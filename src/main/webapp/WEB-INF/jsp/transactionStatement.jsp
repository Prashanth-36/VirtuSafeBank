<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Home</title>
<link rel="stylesheet" href="static/css/home.css" />
</head>
<body>
  <nav>
    <ul class="top-nav">
      <li><img src="static/images/logo.png" alt="" id="logo" /></li>
      <li><a href="customer.html" class="active">My Accounts</a></li>
      <li><a href="moneyTransfer.html">Money Transfer</a></li>
    </ul>
    <a href="" class="top-nav" id="logout">Logout</a>
  </nav>

  <main class="main">
    <form action="" method="get" class="search">
      <label for="accountNo">Account Number</label> <select
        name="senderAccountNo" id="senderAccountNo"
        class="account-selection">
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
      </select>
    </form>
    <table class="border-table">
      <tr>
        <th colspan="8"
          style="text-align: center; background-color: var(--blue); color: white;">
          Transaction Statements</th>
      </tr>
      <tr>
        <th>Transaction ID</th>
        <th>Time</th>
        <th>Transaction type</th>
        <th>Amount</th>
        <th>Primary Account</th>
        <th>Transactional Account</th>
        <th>Description</th>
        <th>Balance</th>
      </tr>
      <tr>
        <td>1</td>
        <td>2024-02-02 hh:mm:ss</td>
        <td>Credit</td>
        <td>5000</td>
        <td>22</td>
        <td>32</td>
        <td>-</td>
        <td>10000</td>
      </tr>
      <tr>
        <td>1</td>
        <td>2024-02-02 hh:mm:ss</td>
        <td>Credit</td>
        <td>5000</td>
        <td>22</td>
        <td>32</td>
        <td>-</td>
        <td>10000</td>
      </tr>
      <tr>
        <td>1</td>
        <td>2024-02-02 hh:mm:ss</td>
        <td>Credit</td>
        <td>5000</td>
        <td>22</td>
        <td>32</td>
        <td>-</td>
        <td>10000</td>
      </tr>
    </table>
  </main>
</body>
</html>
