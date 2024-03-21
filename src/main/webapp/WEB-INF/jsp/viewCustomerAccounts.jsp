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
      <a href="" class="top-nav" id="logout">Logout</a>
    </nav>

    <div class="main-container">
      <aside>
        <ul class="side-nav">
          <li>
            <a href="customer.html" class="active">My Accounts</a>
          </li>
          <li>
            <a href="transactionStatement.html">View Transaction Statement</a>
          </li>
        </ul>
      </aside>
      <main class="main">
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
          <tr>
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
  </body>
</html>
