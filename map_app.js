const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

const cors = require('cors');
app.use(cors());

const db = mysql.createConnection({
  host: 'mysql.sqlpub.com',
  user: 'wi_user',
  password: 'P1q7SrJqSRwH87Dn',
  database: 'weindependent',
  port: 3306
});

db.connect(err => {
  if (err) {
    console.error('Error connecting to database:', err.stack);
    return;
  }
  console.log('Connected to database');
});

app.get('/api/schools/nearby', (req, res) => {
  const { longitude, latitude } = req.query;

  if (!longitude || !latitude) {
    return res.status(400).json({ error: 'Missing longitude or latitude' });
  }
  const query = `
    SELECT name, address, latitude, longitude,
           (6371 * ACOS(COALESCE(COS(RADIANS(?)) * COS(RADIANS(latitude)) 
           * COS(RADIANS(longitude) - RADIANS(?)) + SIN(RADIANS(?)) 
           * SIN(RADIANS(latitude)), 0))) AS distance
    FROM esl_schools
    WHERE latitude IS NOT NULL AND longitude IS NOT NULL
    ORDER BY distance
    LIMIT 10;
`;

  db.query(query, [latitude, longitude, latitude], (err, results) => {
    if (err) {
      console.error(' Database query error:', err.message);
      res.status(500).json({ error: err.message });
      return;
    }
    res.json(results);
  });
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});