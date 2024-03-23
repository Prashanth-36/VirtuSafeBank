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
      <li><a href="accounts.html" class="active">Accounts
          Management</a></li>
      <li><a href="branches.html">Branch Management</a></li>
      <li><a href="">Customer Management</a></li>
      <li><a href="">Employee Management</a></li>
      <li><a href="">Fund Transfer</a></li>
    </ul>
    <a href="" class="top-nav" id="logout">Logout</a>
  </nav>

  <main class="main">
    <table class="table" style="margin-top: 5rem">
      <tr>
        <th colspan="2"
          style="text-align: center; background-color: var(--blue); color: white;">
          Account Details</th>
      </tr>
      <tr>
        <th>Account Number</th>
        <td>1</td>
      </tr>
      <tr>
        <th>Customer ID</th>
        <td>121</td>
      </tr>
      <tr>
        <th>Current Balance</th>
        <td>30000</td>
      </tr>
      <tr>
        <th>Is Primary Accout</th>
        <td>Yes</td>
      </tr>
      <tr>
        <th>Account Open Date</th>
        <td>2023-05-18</td>
      </tr>
      <tr>
        <th>Branch ID</th>
        <td>1223</td>
      </tr>
      <tr>
        <th>Account Status</th>
        <td>Active</td>
      </tr>
    </table>
    <!-- <button
          style="
            font-size: large;
            padding: 0.5rem;
            margin: auto;
            display: block;
            background-color: #009e60;
            color: white;
          "
        >
          Activate
        </button> -->
    <button
      style="font-size: large; padding: 0.5rem; margin: auto; display: block; background-color: #e34234; color: white;">
      Deactivate</button>
  </main>
</body>
</html>
