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
      <li><a href="accounts.html">Accounts Management</a></li>
      <li><a href="branches.html" class="active">Branch
          Management</a></li>
      <li><a href="">Customer Management</a></li>
      <li><a href="">Employee Management</a></li>
      <li><a href="">Fund Transfer</a></li>
    </ul>
    <a href="" class="top-nav" id="logout">Logout</a>
  </nav>

  <main class="main">
    <form action="" method="get" class="search">
      <label for="branchId">Branch ID</label> <input type="search"
        name="branchId" id="branchId" placeholder="Branch ID" />
      <img src="static/images/search.png" alt="" width="50rem" />
    </form>
    <table class="table">
      <tr style="position: relative">
        <th colspan="2"
          style="text-align: center; background-color: var(--blue); color: white;">
          Branch Details <img src="static/images/edit-white.png"
          onclick="window.location.href='branchForm.html?id=1'"
          alt="edit"
          style="position: absolute; font-size: larger; right: 1rem; width: 2rem;" />
        </th>
      </tr>
      <tr>
        <th>Branch ID</th>
        <td>1</td>
      </tr>
      <tr>
        <th>IFSC</th>
        <td>VSB0001</td>
      </tr>
      <tr>
        <th>Location</th>
        <td>street</td>
      </tr>
      <tr>
        <th>City</th>
        <td>Coimbatore</td>
      </tr>
      <tr>
        <th>State</th>
        <td>Tamil Nadu</td>
      </tr>
      <tr>
        <th>Status</th>
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
