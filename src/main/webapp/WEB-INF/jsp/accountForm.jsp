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
          <a href="accountForm.html" class="active">Accounts Management</a>
        </li>
        <li><a href="branches.html">Branch Management</a></li>
        <li><a href="customerManagement.html">Customer Management</a></li>
        <li><a href="">Employee Management</a></li>
        <li><a href="">Fund Transfer</a></li>
      </ul>
      <a href="" class="top-nav" id="logout">Logout</a>
    </nav>

    <div class="main-container">
      <main class="main">
        <form action="" class="form-container">
          <h3>Create Account</h3>
          <label for="customerId">Customer ID *</label>
          <input
            type="text"
            name="customerId"
            id="customerId"
            placeholder="Customer ID"
          />
          <label for="branchId">Branch ID *</label>
          <input
            type="text"
            name="branchId"
            id="branchId"
            placeholder="Branch ID"
          />
          <label for="mpin">MPIN</label>
          <input
            type="password"
            name="mpin"
            id="mpin"
            placeholder="Default MPIN"
          />
          <button>Create</button>
        </form>
      </main>
    </div>
  </body>
</html>
