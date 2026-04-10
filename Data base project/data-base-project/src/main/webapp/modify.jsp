<html>
<style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
    .container { margin: 20px; }
</style>
<body>
<div class="container">
  <h2>Modify the Database</h2>
  <p>Select a table to view its records:</p>

  <form action="testdb" method="GET">
      <input type="hidden" name="action" value="viewTable">

      <div>
          <input type="radio" name="tableName" value="Hotels" required> Hotels<br>
          <input type="radio" name="tableName" value="Chambres"> Rooms<br>
          <input type="radio" name="tableName" value="Employes"> Employees<br>
          <input type="radio" name="tableName" value="Clients"> Clients<br>
      </div>

      <br>
      <button type="submit">View Data</button>
      <button type="button" onclick="history.back()">Retourner</button>
  </form>
</div>
</body>
</html>



