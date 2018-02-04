CREATE TABLE model.vendors(
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(50),
  location_lat DECIMAL,
  location_lng DECIMAL,
  UNIQUE (id)
);

CREATE TABLE model.receipts(
  id INTEGER NOT NULL PRIMARY KEY,
  vendor INTEGER,
  subtotal INTEGER,
  tip INTEGER,
  tax INTEGER,
  total Integer,

  FOREIGN KEY (vendor) REFERENCES model.vendors (id),

  UNIQUE (id)
);

CREATE TABLE model.items(
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(50),
  total INTEGER,
  img VARCHAR(500),
  quantity VARCHAR(50),
  UNIQUE (id)
);