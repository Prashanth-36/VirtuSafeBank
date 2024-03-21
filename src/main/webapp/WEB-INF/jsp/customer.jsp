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
          <a href="customer.html" class="active">My Accounts</a>
        </li>
        <li><a href="moneyTransfer.html">Money Transfer</a></li>
      </ul>
      <img src="images/profile-user.png" alt="" style="width: 2.6rem" />
      <a href="" class="top-nav" id="logout">Logout</a>
    </nav>

    <div class="main-container">
      <main class="main">
        <div class="totalBalance">
          Total Available Balance: ₹<span> 10000</span>
        </div>
        <!-- <div class="options">
          <div
            class="card"
            onclick="window.location.href='viewCustomerAccounts.html';"
          >
            <img src="images/bank-account.png" width="50rem" alt="" />
            <p>View All Accounts</p>
          </div>
          <div class="card">
            <img src="images/financial.png" alt="" width="100rem" />
            <p>Change Primary Account</p>
          </div>
          <div class="card">
            <img src="images/password.png" alt="" width="100rem" />Change MPIN
          </div>
        </div> -->
        <table class="border-table">
          <tr>
            <th
              colspan="7"
              style="
                text-align: center;
                background-color: var(--blue);
                color: white;
              "
            >
              Accounts
            </th>
          </tr>
          <tr>
            <th>Account Number</th>
            <th>Current Balance</th>
            <th>Is Primary Accout</th>
            <th>Account Open Date</th>
            <th>Branch ID</th>
            <th>Account Status</th>
          </tr>
          <tr onclick="window.location.href='customerAccount.html?id=1'">
            <td>1</td>
            <td>30000</td>
            <td>Yes</td>
            <td>2023-05-18</td>
            <td>1223</td>
            <td>Active</td>
          </tr>
          <tr>
            <td>2</td>
            <td>30000</td>
            <td>No</td>
            <td>2024-05-18</td>
            <td>1223</td>
            <td>Active</td>
          </tr>
        </table>
      </main>
    </div>
    <div class="pages" id="pages">
      <a id="previousPage" onclick="previousPage()"
        ><img src="images/left.png" alt="previous" style="width: 1rem"
      /></a>
      <a class="page activePage">1</a>
      <a class="page">2</a>
      <a class="page">3</a>
      <a class="page">4</a>
      <a class="page">5</a>
      <a id="nextPage" onclick="nextPage()"
        ><img src="images/right.png" alt="next" style="width: 1rem"
      /></a>
    </div>

    <script src="script/page.js"></script>
  </body>
</html>
