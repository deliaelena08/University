CREATE TABLE magazine_veterinare (
	id_magazin INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
	longitudine DECIMAL NOT NULL,
	latitudine DECIMAL NOT NULL,
	nume VARCHAR(200),
	non_stop BOOLEAN
);

INSERT INTO magazine_veterinare(longitudine, latitudine, nume, non_stop) VALUES
(46.772581, 23.629044, 'ZOOMANIA', FALSE),
(46.769152, 23.629802, 'ZOOCENTER', FALSE),
(46.776618, 23.611147, 'Royal Pet Shop', FALSE),
(46.790818, 23.615360, 'petvet-sop.ro', FALSE),
(46.767484, 23.602221, 'Animax Cipariu', FALSE),
(46.7637960, 23.6024851, 'Pet Shop', FALSE),
(46.753763, 23.585430, 'Animal District', FALSE),
(46.7444326, 23.5851443, 'Pet Guru', FALSE),
(46.7698989, 23.5925150, 'Rom Zoo Market Impex', FALSE),
(46.750005, 23.605582, 'Buna Ziua Zoo', FALSE),
(46.779981, 23.582348, 'Pet Store Gruia', FALSE),
(46.750374, 23.567196, 'Endler', FALSE),
(46.755098, 23.557799, 'Mercani', FALSE),
(46.7582606, 23.5405107, 'Animax', FALSE),
(46.7526774, 23.5340003, 'Animax', FALSE),
(46.794957, 23.601852, 'Edinor Pet', FALSE);

SELECT * FROM magazine_veterinare;
