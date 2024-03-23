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
    <form action="" class="form-container">
      <h3>Create Branch</h3>
      <!-- <h3>Edit Branch</h3> -->
      <label for="ifsc">IFSC *</label> <input type="text" name="ifsc"
        id="ifsc" placeholder="IFSC" required /> <label for="location">location
        *</label> <input type="text" name="location" id="location"
        placeholder="Location" required /> <label for="city">City
        *</label> <input type="text" name="city" id="city" placeholder="City"
        required /> <label for="city">State *</label> <input
        type="text" name="state" id="state" placeholder="State" required />
      <button>Create</button>
      <!-- <button>Update</button> -->
    </form>
  </main>
</body>
</html>
