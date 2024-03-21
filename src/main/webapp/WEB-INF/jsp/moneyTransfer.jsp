<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="css/home.css" />
  </head>
  <body>
    <nav>
      <ul class="top-nav">
        <li><img src="images/logo.png" alt="" id="logo" /></li>
        <li>
          <a href="customer.html">My Accounts</a>
        </li>
        <li><a href="moneyTransfer.html" class="active">Money Transfer</a></li>
      </ul>
      <a href="" class="top-nav" id="logout">Logout</a>
    </nav>

    <div class="main-container">
      <aside>
        <ul class="side-nav">
          <li>
            <a href="moneyTransfer.html" class="active">Money Transfer</a>
          </li>
          <li>
            <a href="withdrawl.html">Withdrawl</a>
          </li>
          <li>
            <a href="deposit.html">Deposit</a>
          </li>
        </ul>
      </aside>
      <main class="main">
        <div class="transfer-options">
          <div class="card" id="withinBank">
            <img src="images/banking.png" width="100rem" alt="" />
            <p>Within Bank Transaction</p>
          </div>
          <div class="card" id="otherBank">
            <img src="images/bank-to-bank.png" alt="" width="100rem" />
            <p>Other Bank Transaction</p>
          </div>
        </div>
        <form
          id="form"
          action=""
          method="get"
          class="form-container"
          style="display: none; height: fit-content"
        >
          <label for="senderAccountNo"
            >Select From Account Number
            <select
              name="senderAccountNo"
              id="senderAccountNo"
              class="account-selection"
            >
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
            </select>
          </label>
          <input
            type="text"
            name="beneficiaryAccNo"
            id="beneficiaryAccNo"
            placeholder="Beneficiary Account No"
          />
          <input
            type="text"
            name="beneficiaryIfsc"
            id="beneficiaryIfsc"
            placeholder="Beneficiary IFSC code"
          />
          <input type="text" name="amount" id="amount" placeholder="Amount" />
          <input type="password" name="mpin" id="mpin" placeholder="MPIN" />
          <button>Send</button>
        </form>
      </main>
    </div>

    <script>
      const form = document.getElementById("form");
      const withinBank = document.getElementById("withinBank");
      const otherBank = document.getElementById("otherBank");
      withinBank.addEventListener("click", () => {
        const classes = withinBank.classList;
        if (!classes.contains("selected")) {
          withinBank.classList.add("selected");
          otherBank.classList.remove("selected");
          form.style.display = "flex";
          document.getElementById("beneficiaryIfsc").style.display = "none";
        }
      });
      otherBank.addEventListener("click", () => {
        const classes = otherBank.classList;
        if (!classes.contains("selected")) {
          otherBank.classList.add("selected");
          withinBank.classList.remove("selected");
          form.style.display = "flex";
          document.getElementById("beneficiaryIfsc").style.display = "block";
        }
      });
    </script>
  </body>
</html>
