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
          <a href="accounts.html">Accounts Management</a>
        </li>
        <li>
          <a href="branches.html" class="active">Branch Management</a>
        </li>
        <li><a href="">Customer Management</a></li>
        <li><a href="">Employee Management</a></li>
        <li><a href="">Fund Transfer</a></li>
      </ul>
      <a href="" class="top-nav" id="logout">Logout</a>
    </nav>

    <div class="main-container">
      <main class="main">
        <form action="" method="get" class="search">
          <label for="branchId">Branch ID</label>
          <input
            type="search"
            name="branchId"
            id="branchId"
            placeholder="Branch ID"
          />
          <img src="images/search.png" alt="" width="50rem" />
        </form>
        <table id="table" class="border-table">
          <tr>
            <th
              colspan="6"
              style="
                text-align: center;
                position: relative;
                background-color: var(--blue);
                color: white;
              "
            >
              Branch Details
              <button
                onclick="window.location.href='branchForm.html'"
                style="
                  position: absolute;
                  font-size: larger;
                  right: 1rem;
                  width: 2rem;
                "
              >
                +
              </button>
            </th>
          </tr>
          <tr>
            <th>Branch ID</th>
            <th>IFSC</th>
            <th>Location</th>
            <th>City</th>
            <th>State</th>
            <th>Branch Status</th>
          </tr>
          <tr onclick="window.location.href='viewBranch.html?id=1'">
            <td>1</td>
            <td>VSB0001</td>
            <td>1st street</td>
            <td>Coimbatore</td>
            <td>Tamil Nadu</td>
            <td>Active</td>
          </tr>
          <tr onclick="window.location.href='viewBranch.html?id=2'">
            <td>1</td>
            <td>VSB0001</td>
            <td>1st street</td>
            <td>Coimbatore</td>
            <td>Tamil Nadu</td>
            <td>Active</td>
          </tr>
        </table>
      </main>
    </div>
    <div class="pages" id="pages">
      <a id="previousPage" onclick="previousPage()"
        ><img src="images/left.png" alt="previous" style="width: 1rem"
      /></a>
      <a href="" class="page activePage">1</a>
      <a href="" class="page">2</a>
      <a href="" class="page">3</a>
      <a href="" class="page">4</a>
      <a href="" class="page">5</a>
      <a id="nextPage" onclick="nextPage()"
        ><img src="images/right.png" alt="next" style="width: 1rem"
      /></a>
    </div>

    <script src="script/page.js"></script>
  </body>
</html>
