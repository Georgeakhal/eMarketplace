const div = document.querySelector(".posts");

if (div) {
  fetch("http://localhost:8080/product")
    .then((res) => {
      if (!res.ok) {
        throw new Error(`HTTP error! Status: ${res.status}`);
      }
      return res.json();
    })
    .then((data) => {
      data.forEach((product) => {
        const iName = document.createElement("p");
        iName.textContent = product.name;
        div.appendChild(iName);

        const iPrice = document.createElement("p");
        iPrice.textContent = product.price;
        div.appendChild(iPrice);

        if (product.photoUrl && typeof product.photoUrl === "string") {
          console.log("Image URL:", product.photoUrl); // should now log valid URLs

          const img = document.createElement("img");
          img.src = product.photoUrl;
          img.alt = product.name || "Product image";
          img.width = 300;

          div.appendChild(img);
        }


      });
    })
    .catch((error) => {
      console.error("Fetch error:", error);
    });
}


// Only run this block if form is found (means you're on create.html)
const form = document.querySelector(".submitPost");

if (form) {
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const fname = form.querySelector('[name="name"]').value;
    const fprice = form.querySelector('[name="price"]').value;
    const fdescription = form.querySelector('[name="description"]').value;
    let imgInput = form.querySelector('[name="img"]');
    let file = imgInput.files[0];

    if (!file) {
      const jsonData = {
        name: fname,
        price: fprice,
        description: fdescription,
        photoUrl: null,
      };

      submitProduct(jsonData);
    } else {
      const reader = new FileReader();

      reader.onload = function (event) {
        const base64Image = event.target.result;

        const jsonData = {
          name: fname,
          price: fprice,
          description: fdescription,
          photoUrl: base64Image,
        };

        submitProduct(jsonData);
      };

      reader.readAsDataURL(file);
    }
  });
}

function submitProduct(jsonData) {
  fetch("http://localhost:8080/product", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(jsonData),
  })
    .then((res) => res.json())
    .then((json) => {
      console.log("Success:", json);
      window.location.href = "./index.html";
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

function switchPage(url) {
  window.location.href = url;
}
