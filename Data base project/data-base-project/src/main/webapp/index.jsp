<html>
<head>
  <meta charset="UTF-8">
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
</head>
<body>
    <h2>Hotel System</h2>

    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="list">
        <button type="submit">View All Hotels</button>
    </form>
    <h3>Recherche des chambres par critères</h3>
    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="search">
        <div>
            <input type="checkbox" name="useDate" value="true"> Filtrer par Date:
            <input type="date" name="sDateVal">
            <input type="date" name="eDateVal">
        </div>
        <div>
            <input type="checkbox" name="useCap" value="true"> Filtrer par Capacité:
            <input type="number" name="capVal" placeholder="e.x. 5">
        </div>
        <div>
            <input type="checkbox" name="useSup" value="true"> Filtrer par Superficie:
            <input type="number" name="superVal" placeholder="e.x. 350">
        </div>
        <div>
            <input type="checkbox" name="useChain" value="true"> Filtrer par Chaîne:
            <input type="text" name="chainVal" placeholder="e.x. MGM Resorts">
        </div>
        <div>
            <input type="checkbox" name="useCat" value="true"> Filtrer par Catégorie:
            <input type="number" name="CatVal" placeholder="e.x. 4">
        </div>
        <div>
            <input type="checkbox" name="usePrice" value="true"> Filtrer par Prix:
            <input type="number" name="PriceVal" placeholder="e.x. 400">
        </div>
        <button type="submit">Continuer</button>
          <h3>Client Sign Up</h3>

        <form>
            <div>
                Client ID: <input type="number" name="clID" placeholder="e.x. 54">
            </div>
            <div>
                Name: <input type="text" name="name" placeholder="e.x. John Doe">
            </div>
            <div>
                Address: <input type="text" name="address" placeholder="e.x. 613 park avenue">
            </div>
            <div>
                NAS: <input type="number" name="NAS" placeholder="e.x. 123456789">
            </div>
            <button type="submit">Continuer</button>

        </form>
    </form>
        <h3>Fait une Réservation</h3>
        <form>
            <div>
                Room ID: <input type="number" name="roomID" placeholder="e.x. 54">
            </div>
             <div>
                Client ID: <input type="number" name="clientID" placeholder="e.x. 54">
            </div>
            <button type="submit">Continuer</button>

        </form>
        <h3>Fait une Location</h3>
        <form>
            <div>
                Room ID: <input type="number" name="rID" placeholder="e.x. 54">
            </div>
             <div>
                Client ID: <input type="number" name="cID" placeholder="e.x. 54">
            </div>
            <div>
                Employee ID: <input type="number" name="empID" placeholder="e.x. 54">
            </div>
            <button type="submit">Continuer</button>

        </form>

</body>
</html>