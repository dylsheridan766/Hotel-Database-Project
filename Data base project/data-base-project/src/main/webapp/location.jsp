<html>
<style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
<h3>Fait une Location</h3>
        <form action="testdb" method="GET">
            <div>
                Room ID: <input type="number" name="rID" placeholder="e.x. 54">
            </div>
             <div>
                Client Email: <input type="text" name="cEM" placeholder="e.x. shmebulock@gmail.com">
            </div>
            <div>
                Employee ID: <input type="number" name="empID" placeholder="e.x. 54 (optionnel)">
            </div>
             <div>
                End Date: <input type="date" name="endate" placeholder="2026/04/14">
            </div>
            <input type="hidden" name="action" value="locappend">
            <button type="submit">Soumettre</button>
           <button onclick="history.back()">Retourner</button>
        </form>
</html>