const token = localStorage.getItem("token");
const userString = localStorage.getItem("user");

const headDiv = document.getElementsByClassName("headers")[0];

// --1

document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  const userString = localStorage.getItem("user");

  if (token && userString) {
    const user = JSON.parse(userString);

    const usernameSpan = document.querySelector(".username");
    if (usernameSpan) {
      usernameSpan.textContent = user.username;
    }

    const userInfoDiv = document.querySelector(".user-info");
    if (userInfoDiv) {
      const button = document.createElement("button");
      button.textContent = "My Item";
      button.onclick = () => switchToPage("./newItem.html");
      button.style.display = "inline-block";
      userInfoDiv.insertBefore(button, userInfoDiv.querySelector("button"));
    }
  }
});


// --2
let totalPages = 0;
const size = 6;
const postdiv = document.querySelector(".posts");
const pageNumEl = document.querySelector(".pageButtons p");

let selectedItem = "dateDesc";
const selectSort = document.getElementById("sort");

fetchAndRenderPage(0);

function fetchAndRenderPage(pageIndex) {
  const url = `http://localhost:8080/product?page=${pageIndex}&size=${size}&sort=${encodeURIComponent(
    selectedItem
  )}`;

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
      if (!postdiv) return;

      postdiv.innerHTML = "";

      if (Array.isArray(data)) {
        postdiv.innerHTML = "";

        data.forEach((product) => {
          const div = document.createElement("div");
          div.className = "post";

          if (product.photoUrl) {
            const img = document.createElement("img");
            img.src = product.photoUrl;
            img.alt = product.name || "Product image";
            div.appendChild(img);
          }

          const iName = document.createElement("h3");
          iName.textContent = product.name;
          iName.style.cursor = "pointer";
          iName.onclick = () => {
            detailedPostPage(
              product.name,
              product.description,
              product.photoUrl,
              product.price,
              product.date,
              product.user.username
            );
          };
          div.appendChild(iName);

          const iPrice = document.createElement("p");
          iPrice.textContent = `${product.price}$`;
          div.appendChild(iPrice);

          postdiv.appendChild(div);
        });
      } else {
        console.warn("Product data missing or invalid:", data);
        const msg = document.createElement("div");
        msg.innerHTML = "<p>No products available.</p>";
        postdiv.appendChild(msg);
      }
    })
    .catch((error) => {
      console.error("Fetch error:", error);
    });
}

function switchPage(delta) {
  console.log(delta);
  const currentPage = parseInt(pageNumEl.textContent.trim(), 10);
  if (isNaN(currentPage)) return;

  const newPage = currentPage + delta;
  if (newPage < 1 || newPage > totalPages) return;

  fetchAndRenderPage(newPage - 1);
}

selectSort.addEventListener("change", function () {
  selectedItem = selectSort.value;
  fetchAndRenderPage(0);
});

// --1

function detailedPostPage(name, description, photoUrl, price, date, username) {
  const productData = {
    name,
    description,
    photoUrl,
    price,
    date,
    username,
  };

  localStorage.setItem("selectedProduct", JSON.stringify(productData));
  window.location.href = "./item.html";
}

function switchToPage(url) {
  window.location.href = url;
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("user");

  window.location.href = "./login.html";
}