const token = localStorage.getItem("token");
const userString = localStorage.getItem("user");

const postDiv = document.querySelector(".post");

if (postDiv) {
  const productData = JSON.parse(localStorage.getItem("selectedProduct"));
  const user = JSON.parse(userString);

  const usernameHeader = document.querySelector("header .username");
  if (user && user.username && usernameHeader) {
    usernameHeader.textContent = `Logged in as: ${user.username}`;
  }

  const nameEl = document.querySelector(".post-title");
  const descEl = document.querySelector(".description");
  const priceEl = document.querySelector(".price");
  const usernameEl = document.querySelector(".user");
  const dateEl = document.querySelector(".date");
  const imgEl = document.querySelector(".img");

  nameEl.textContent = productData.name;
  descEl.textContent = productData.description;
  priceEl.textContent = `Price: ${productData.price}`;
  usernameEl.textContent = productData.username;

  if (productData.date) {
    dateEl.textContent = dayjs(productData.date).format("YYYY-MM-DD HH:mm");
  }

  if (productData.photoUrl && typeof productData.photoUrl === "string") {
    imgEl.src = productData.photoUrl;
  }
}

if (token && userString) {
  console.log("Logged in");

  const user = JSON.parse(userString);

  const form = document.querySelector(".submitPost");

  if (form) {
    form.addEventListener("submit", function (e) {
      e.preventDefault();

      const fname = form.querySelector('[name="name"]').value;
      const fprice = form.querySelector('[name="price"]').value;
      const fdescription = form.querySelector('[name="description"]').value;
      const imgInput = form.querySelector('[name="img"]');
      const file = imgInput.files[0];

      if (!file) {
        alert("Image is required.");
        return;
      }

      const reader = new FileReader();

      reader.onload = function (event) {
        const base64Image = event.target.result;

        const jsonData = {
          name: fname,
          price: fprice,
          description: fdescription,
          photoUrl: base64Image,
          userId: user.id,
        };

        submitProduct(jsonData);
      };

      reader.readAsDataURL(file);
    });
  }

  function submitProduct(jsonData) {
    fetch("http://localhost:8080/product", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        token: token,
      },
      body: JSON.stringify(jsonData),
    })
      .then(async (res) => {
        const contentType = res.headers.get("content-type");
        const hasJson = contentType && contentType.includes("application/json");

        if (!res.ok) {
          const errorData = hasJson ? await res.json() : { message: res.statusText };

          if (res.status === 403 || res.status === 401) {
            console.warn("Authentication failed:", errorData.message);
            alert("Session expired. Please log in again.");
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "./login.html";
            return Promise.reject("Unauthorized");
          }

          throw new Error(errorData.message || `HTTP error! status: ${res.status}`);
        }

        if (res.status === 204 || !hasJson) {
          return null;
        }

        return res.json();
      })
      .then((json) => {
        console.log("Success:", json);
        window.location.href = "./index.html";
      })
      .catch((error) => {
        console.error("Error:", error.message);
      });
  }


}