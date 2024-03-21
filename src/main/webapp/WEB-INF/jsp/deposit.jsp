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
            <a href="moneyTransfer.html">Money Transfer</a>
          </li>
          <li>
            <a href="withdrawl.html">Withdrawl</a>
          </li>
          <li>
            <a href="deposit.html" class="active">Deposit</a>
          </li>
        </ul>
      </aside>
      <main class="main">
        <form action="" method="get" class="form-container">
          <label for="accountNo"
            >Select Account Number
            <select name="accountNo" id="accountNo" class="account-selection">
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
            </select>
          </label>

          <input type="text" name="amount" id="amount" placeholder="Amount" />
          <input type="password" name="mpin" id="mpin" placeholder="MPIN" />
          <button>Deposit</button>
        </form>
      </main>
    </div>
  </body>
</html>
