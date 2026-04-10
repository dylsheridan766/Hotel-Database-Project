<html>
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css2?family=Hind&family=Source+Sans+3&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <style>
    body {
      font-family: 'Source Sans 3', sans-serif;
    }
  </style>
</head>
<body>


  <div style="display: flex; align-items: center; gap: 8px;">
    <i class="fa-regular fa-user" style="font-size: 20px;"></i>
    <h3 style="margin: 0;color: #B11116;">Client Sign Up</h3>
  </div>

  <form action="testdb" method="GET">
    <div>
      Name: <input type="text" name="name" placeholder="e.x. Jane Doe">
    </div>
    <div>
      Address: <input type="text" name="address" placeholder="e.x. 613 park avenue">
    </div>
    <div>
      E-mail: <input type="text" name="email" placeholder="e.x. scientologytoday@gmail.com">
    </div>
    <div>
      NAS: <input type="number" name="NAS" placeholder="e.x. 123456789">
    </div>
    <input type="hidden" name="action" value="append">
    <button type="submit">Soumettre</button>
  </form>
  <a href="index.jsp" style="text-decoration: none;">
    <button type="button">Retourner</button>
  </a>
</body>
</html>