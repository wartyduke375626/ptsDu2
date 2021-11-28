PRAGMA foreign_keys = ON;


/* stops */
INSERT INTO stop(sid, sname) VALUES (1, 'STOP A');
INSERT INTO stop(sid, sname) VALUES (2, 'STOP B');
INSERT INTO stop(sid, sname) VALUES (3, 'STOP C');
INSERT INTO stop(sid, sname) VALUES (4, 'STOP D');
INSERT INTO stop(sid, sname) VALUES (5, 'STOP E');
INSERT INTO stop(sid, sname) VALUES (6, 'STOP F');
INSERT INTO stop(sid, sname) VALUES (7, 'STOP G');
INSERT INTO stop(sid, sname) VALUES (8, 'STOP H');
INSERT INTO stop(sid, sname) VALUES (9, 'STOP I');
INSERT INTO stop(sid, sname) VALUES (10, 'STOP J');


/* lines */
INSERT INTO line(lid, lname, firstStop) VALUES (1, 'L1', 2);
INSERT INTO line(lid, lname, firstStop) VALUES (2, 'L2', 6);
INSERT INTO line(lid, lname, firstStop) VALUES (3, 'L3', 10);
INSERT INTO line(lid, lname, firstStop) VALUES (4, 'L4', 1);
INSERT INTO line(lid, lname, firstStop) VALUES (5, 'L5', 7);


/* stops-lines */
INSERT INTO stop_line(sid, lid) VALUES (1, 4);

INSERT INTO stop_line(sid, lid) VALUES (2, 1);
INSERT INTO stop_line(sid, lid) VALUES (3, 1);
INSERT INTO stop_line(sid, lid) VALUES (3, 3);

INSERT INTO stop_line(sid, lid) VALUES (4, 1);
INSERT INTO stop_line(sid, lid) VALUES (4, 4);
INSERT INTO stop_line(sid, lid) VALUES (4, 5);

INSERT INTO stop_line(sid, lid) VALUES (5, 1);
INSERT INTO stop_line(sid, lid) VALUES (5, 2);

INSERT INTO stop_line(sid, lid) VALUES (6, 2);

INSERT INTO stop_line(sid, lid) VALUES (7, 3);
INSERT INTO stop_line(sid, lid) VALUES (7, 5);

INSERT INTO stop_line(sid, lid) VALUES (8, 2);
INSERT INTO stop_line(sid, lid) VALUES (8, 3);
INSERT INTO stop_line(sid, lid) VALUES (8, 4);
INSERT INTO stop_line(sid, lid) VALUES (8, 5);

INSERT INTO stop_line(sid, lid) VALUES (9, 2);
INSERT INTO stop_line(sid, lid) VALUES (9, 3);


/* lineSegments */
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (1, 1, 0, 7, 3);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (2, 1, 1, 4, 4);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (3, 1, 2, 3, 5);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (4, 1, 3, 8, 6);

INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (5, 2, 0, 8, 6);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (6, 2, 1, 5, 9);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (7, 2, 2, 2, 8);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (8, 2, 3, 7, 10);

INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (9, 3, 0, 7, 8);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (10, 3, 1, 5, 7);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (11, 3, 2, 3, 3);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (12, 3, 3, 7, 2);

INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (13, 4, 0, 6, 4);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (14, 4, 1, 4, 8);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (15, 4, 2, 2, 9);

INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (16, 5, 0, 5, 8);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (17, 5, 1, 4, 4);
INSERT INTO lineSegment(lsid, lid, sIndex, timeDiff, nextStop) VALUES (18, 5, 2, 6, 1);


/* buses */
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (1, 1, 10, 10);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (2, 1, 30, 10);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (3, 1, 50, 10);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (4, 1, 70, 10);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (5, 1, 90, 10);

INSERT INTO bus(bid, lid, startTime, capacity) VALUES (6, 2, 15, 8);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (7, 2, 40, 8);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (8, 2, 65, 8);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (9, 2, 90, 8);

INSERT INTO bus(bid, lid, startTime, capacity) VALUES (10, 3, 10, 7);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (11, 3, 35, 7);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (12, 3, 60, 7);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (13, 3, 85, 7);

INSERT INTO bus(bid, lid, startTime, capacity) VALUES (14, 4, 20, 5);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (15, 4, 50, 5);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (16, 4, 80, 5);

INSERT INTO bus(bid, lid, startTime, capacity) VALUES (17, 5, 30, 6);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (18, 5, 60, 6);
INSERT INTO bus(bid, lid, startTime, capacity) VALUES (19, 5, 90, 6);


/* busSegments */
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (1, 1, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (1, 2, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (1, 3, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (1, 4, 10, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (2, 1, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (2, 2, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (2, 3, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (2, 4, 10, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (3, 1, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (3, 2, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (3, 3, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (3, 4, 10, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (4, 1, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (4, 2, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (4, 3, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (4, 4, 10, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (5, 1, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (5, 2, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (5, 3, 10, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (5, 4, 10, 0);


INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (6, 5, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (6, 6, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (6, 7, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (6, 8, 8, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (7, 5, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (7, 6, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (7, 7, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (7, 8, 8, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (8, 5, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (8, 6, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (8, 7, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (8, 8, 8, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (9, 5, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (9, 6, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (9, 7, 8, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (9, 8, 8, 0);


INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (10, 9, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (10, 11, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (10, 10, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (10, 12, 7, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (11, 9, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (11, 11, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (11, 10, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (11, 12, 7, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (12, 9, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (12, 11, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (12, 10, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (12, 12, 7, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (13, 9, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (13, 11, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (13, 10, 7, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (13, 12, 7, 0);


INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (14, 13, 5, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (14, 14, 5, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (14, 15, 5, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (15, 13, 5, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (15, 14, 5, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (15, 15, 5, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (16, 13, 5, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (16, 14, 5, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (16, 15, 5, 0);


INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (17, 16, 6, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (17, 17, 6, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (17, 18, 6, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (18, 16, 6, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (18, 17, 6, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (18, 18, 6, 0);

INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (19, 16, 6, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (19, 17, 6, 0);
INSERT INTO busSegment(bid, lsid, capacity, passengers) VALUES (19, 18, 6, 0);
