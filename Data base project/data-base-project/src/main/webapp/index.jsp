<html>
<body>
    <h2>Hotel System</h2>
    
    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="list">
        <button type="submit">View All Hotels</button>
    </form>

    <h3>Recherche des chambre par criteres</h3>
<form action="testdb" method="GET">
    <input type="hidden" name="action" value="search">

    <div>
        <input type="checkbox" name="useDate" value="true"> Filtrer par Date: 
       Date de départ: <input type="text" name="sDateVal" placeholder="format YYYY/MM/DD">
       Date de finisement: <input type="text" name="eDateVal" placeholder="format YYYY/MM/DD">
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



    <button type="submit">Continue</button>
</form>

</body>
</html>