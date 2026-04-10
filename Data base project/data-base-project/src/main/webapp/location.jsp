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
                Client ID: <input type="number" name="cID" placeholder="e.x. 54">
            </div>
            <div>
                Employee ID: <input type="number" name="empID" placeholder="e.x. 54">
            </div>
            <input type="hidden" name="action" value="locappend">
            <button type="submit">Soumettre</button>
           <button onclick="history.back()">Retourner</button>
        </form>
</html>