SELECT c.Food, c.Serving, c.Calories, c.Carbs, c.Protein, c.Sugar,
	c.Sodium, c.Fat, c.Cholest, c.Fiber, c.Water, c.FruitVeggie, c.Last, c.ServingsLast, c.ServingsLastE, c.LastE,
	sla.ServingsLast as ThisServingsLast,
	sle.ServingsLastE as ThisServingsLastE
FROM Core.Fit_Calories c
LEFT JOIN (SELECT ServingsLast, Last FROM Core.Fit_Calories WHERE Last=CURDATE() LIMIT 1 ) AS sla ON sla.Last = c.Last
LEFT JOIN (SELECT ServingsLastE, Last FROM Core.Fit_Calories WHERE Last=CURDATE() LIMIT 1 ) AS sle ON sle.Last = c.Last;
