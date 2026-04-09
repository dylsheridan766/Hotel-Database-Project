<html>
<style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
<h3>Fait une Réservation</h3>
        <form>
            <div>
                Room ID: <input type="number" name="roomID" placeholder="e.x. 54">
            </div>
             <div>
                Client ID: <input type="number" name="clientID" placeholder="e.x. 54">
            </div>
            <form action="testdb" method="GET">
            <input type="hidden" name="action" value="append">
            <button type="submit">Soumettre</button>
            </form>
           <button onclick="history.back()">Retourner</button>

        </form>
</html>