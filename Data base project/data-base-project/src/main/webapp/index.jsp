<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css2?family=Hind&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Hind&family=Source+Sans+3&display=swap" rel="stylesheet">
  <style>
    body {
    font-family: 'Source Sans 3', sans-serif;
    }
  </style>
</head>
<body>
    <h2 style="display: flex; align-items: center; gap: 10px; font-family: 'Hind', sans-serif; letter-spacing: 0px; color: #B11116;">
  <img src="MichelinStar.svg.png" style="height: 40px;">
  Hotel System
</h2>
    <div style="display: flex; gap: 8px;">
    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="list">
        <button type="submit">View All Chains</button>
    </form>
    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="roombyzone">
        <button type="submit">Rooms by Zone</button>
    </form>
    <form action="testdb" method="GET">
    <input type="hidden" name="action" value="capbyhotel">
    <button type="submit">Capacity by Hotel</button>
    </form>
    </div>
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
            <img src="1594252-200.png" style="height: 24px;">
            <h3 style="margin: 0;">Devenir un Client!</h3>
          </div>
            <form action="clientsignup.jsp" method="GET">
                <button type="submit">Enregistrer!</button>
            </form>
        <div style="display: flex; align-items: center; gap: 8px;">
          <img src="2192.png" style="height: 24px;">
          <h3 style="margin: 0;">Réservations</h3>
        </div>
        <form action="reservation.jsp" method="GET">
                <button type="submit">Reserver</button>
        </form>
        <div style="display: flex; align-items: center; gap: 8px;">
          <img src="66901.png" style="height: 24px;">
          <h3 style="margin: 0;">Locations</h3>
        </div>
          <form action="location.jsp" method="GET">
                <button type="submit">Louer</button>
        </form>
        <div style="display: flex; align-items: center; gap: 8px;">
          <img src="3082103-200.png" style="height: 24px;">
          <h3 style="margin: 0;">Modifier le DB</h3>
        </div>
          <form action="modify.jsp" method="GET">
                <button type="submit">Adjuster</button>
        </form>


</body>
</html>