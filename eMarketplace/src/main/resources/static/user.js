const regform = document.getElementById("userReg");

if (regform) {
  regform.addEventListener("submit", function (e) {
    e.preventDefault();

    const fusername = regform.querySelector('[name="username"]').value;
    const fpassword = regform.querySelector('[name="password"]').value;
    const femail = regform.querySelector('[name="email"]').value;
    const fbirthdayRaw = regform.querySelector('[name="birthday"]').value;
    const fbirthday = new Date(fbirthdayRaw).toISOString().split("T")[0];

    const jsonData = {
      username: fusername,
      password: fpassword,
      email: femail,
      birthday: fbirthday,
    };

    fetch("http://localhost:8080/user/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    })
      .then(async (res) => {
        if (!res.ok) {
          throw new Error("Registration failed with status " + res.status);
        }

        if (res.status === 204 || res.headers.get("content-length") === "0") {
          return null;
        }

        const contentType = res.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
          return res.json();
        }

        return null;
      })
      .then(() => {
        console.log("Registration successful");
        switchPage("./login.html");
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  });
}


const logfrom = document.getElementById("userlog");

if (logfrom) {
  logfrom.addEventListener("submit", function (e) {
    e.preventDefault();

    const fusername = logfrom.querySelector('[name="username"]').value;
    const fpassword = logfrom.querySelector('[name="password"]').value;

    const jsonData = {
      username: fusername,
      password: fpassword,
    };

    fetch("http://localhost:8080/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    })
      .then((res) => res.json())
      .then((json) => {
        const token = json.token;
        const user = json.user;

        localStorage.setItem("token", token);
        console.log(JSON.stringify(user))
        localStorage.setItem("user", JSON.stringify(user));

        alert("Login successful!");
        switchPage("./index.html");
      })
      .catch((error) => {
        console.log("Error: ", error);
      });
  });
}

function switchPage(url) {
  window.location.href = url;
}

