<html>
<style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
<h3>Fait une Réservation</h3>
        <form action="testdb" method="GET">
            <div>
                Room ID: <input type="number" name="roomID" placeholder="e.x. 54">
            </div>
             <div>
                Client Email: <input type="text" name="cEM" placeholder="e.x. bilbobaggins@gmail.com">
            </div>
            <div>
                Start Date: <input type="date" name="sdate" placeholder="2026/04/09">
            </div>
            <div>
                End Date: <input type="date" name="edate" placeholder="2026/04/14">
            </div>
            <input type="hidden" name="action" value="resappend">
            <button type="submit">Soumettre</button>
           <button onclick="history.back()">Retourner</button>

        </form>
</html>