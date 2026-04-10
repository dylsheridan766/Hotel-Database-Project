<html>
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css2?family=Cinzel&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
</head>
<body>
    <h2 style="display: flex; align-items: center; gap: 10px; font-family: 'Cinzel', serif; letter-spacing: 4px;">
          <img src="MichelinStar.svg.png" style="height: 40px;">
          Hotel System
        </h2>

    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="list">
        <button type="submit">View All Chains</button>
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
    </form>
          <div style="display: flex; align-items: center; gap: 8px;">
            <img src="user-icon.png" style="height: 24px;">
            <h3 style="margin: 0;">Devenir un Client!</h3>
          </div>
            <form action="clientsignup.jsp" method="GET">
                <button type="submit">Enregistrer!</button>
            </form>
        <h3>Réservations</h3>
        <form action="reservation.jsp" method="GET">
                <button type="submit">Reserver</button>
        </form>
        <h3>Locations</h3>
          <form action="location.jsp" method="GET">
                <button type="submit">Louer</button>
        </form>
        <h3>Modifier le DB</h3>
          <form action="modify.jsp" method="GET">
                <button type="submit">Adjuster</button>
        </form>


</body>
</html>