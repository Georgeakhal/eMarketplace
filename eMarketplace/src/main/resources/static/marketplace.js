let totalPages = 0;
const size = 6;
const div = document.querySelector(".posts");
const pageNumEl = document.querySelector(".pageButtons p");

fetchAndRenderPage(0);

function fetchAndRenderPage(pageIndex) {
  const url = `http://localhost:8080/product?page=${pageIndex}&size=${size}`;

  fetch(url)
    .then((res) => {
      if (!res.ok) throw new Error(`HTTP error! Status: ${res.status}`);
      return res.json();
    })
    .then((result) => {
      console.log("Raw result from backend:", result);

      const data = result.content ?? result;
      totalPages = result.totalPages ?? 1;

      console.log("Parsed products to render:", data);

      if (pageNumEl) pageNumEl.textContent = pageIndex + 1;
      if (!div) return;

      div.innerHTML = "";

      if (Array.isArray(data)) {
        data.forEach((product) => {
          const iPrice = document.createElement("p");
          iPrice.textContent = `Price: ${product.price}`;
          div.appendChild(iPrice);

          if (product.photoUrl) {
            const img = document.createElement("img");
            img.src = product.photoUrl;
            img.alt = product.name || "Image";
            img.width = 300;
            div.appendChild(img);
          }

          const iName = document.createElement("a");
          iName.textContent = product.name;
          iName.href = "#";
          iName.onclick = () => {
            detailedPostPage(
              product.name,
              product.description,
              product.photoUrl,
              product.price
            );
          };
          div.appendChild(iName);
        });
      } else {
        console.warn("Data is not an array", data);
      }
    })
    .catch((error) => {
      console.error("Fetch error:", error);
    });
}


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

function switchToPage(url) {
  window.location.href = url;
}

function detailedPostPage(name, description, photoUrl, price) {
  const productData = {
    name,
    description,
    photoUrl,
    price,
  };

  localStorage.setItem("selectedProduct", JSON.stringify(productData));
  window.location.href = "./item.html";
}

function switchPage(delta) {
  console.log(delta)
  const currentPage = parseInt(pageNumEl.textContent);
  if (isNaN(currentPage)) return;

  const newPage = currentPage + delta;
  if (newPage < 1 || newPage > totalPages) return;

  fetchAndRenderPage(newPage - 1);
}

// This block should only run if .post exists (on item.html)
document.addEventListener("DOMContentLoaded", () => {
  const postDiv = document.querySelector(".post");
  const productData = JSON.parse(localStorage.getItem("selectedProduct"));

  if (!postDiv || !productData) return;

  const nameEl = document.createElement("h2");
  nameEl.textContent = productData.name;

  const descEl = document.createElement("p");
  descEl.textContent = productData.description;

  const priceEl = document.createElement("p");
  priceEl.textContent = `Price: ${productData.price}`;

  postDiv.appendChild(nameEl);

  if (productData.photoUrl && typeof productData.photoUrl === "string") {
    const img = document.createElement("img");
    img.src = productData.photoUrl;
    img.alt = productData.name || "Product image";
    img.width = 100;
    postDiv.appendChild(img);
  }

  postDiv.appendChild(descEl);
  postDiv.appendChild(priceEl);
});
