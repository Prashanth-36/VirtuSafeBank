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
        <div class="options" style="margin-top: 3rem">
          <div
            class="card"
            onclick="window.location.href=window.location.href+'&setPrimary=1'"
          >
            <img src="images/financial.png" alt="" width="50rem" />
            <p>Set as Primary Account</p>
          </div>
          <div class="card" onclick="toggle()">
            <img src="images/password.png" alt="" width="50rem" />Change MPIN
          </div>
        </div>

        <table class="border-table">
          <tr>
            <th
              colspan="8"
              style="
                text-align: center;
                background-color: var(--blue);
                color: white;
              "
            >
              Transaction Statements
            </th>
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
      </main>
    </div>

    <div id="back-drop" class="back-drop" onclick="toggle()">
      <form
        onclick="event.stopPropagation()"
        action=""
        id="form"
        style="width: 40%; height: fit-content"
        class="form-container"
      >
        <h3>Change MPIN</h3>
        <input
          type="password"
          name="oldMpin"
          id="oldMpin"
          placeholder="Current MPIN"
        />
        <input
          type="password"
          name="newMpin"
          id="newMpin"
          placeholder="New MPIN"
        />
        <input
          type="password"
          name="confirmMpin"
          id="comfirmMpin"
          placeholder="Confirm MPIN"
        />
        <button>Update</button>
      </form>
    </div>
    <script>
      function toggle() {
        const backDrop = document.getElementById("back-drop");
        if (backDrop.style.display === "flex") {
          backDrop.style.display = "none";
        } else {
          backDrop.style.display = "flex";
        }
      }
    </script>
  </body>
</html>
