SELECT c.Food, c.Serving, c.Calories, c.Carbs, c.Protein, c.Sugar,
	c.Sodium, c.Fat, c.Cholest, c.Fiber, c.Water, c.FruitVeggie, c.Last, c.ServingsLast, c.ServingsLastE, c.LastE,
	sla.ServingsLast as ThisServingsLast,
	sle.ServingsLastE as ThisServingsLastE
FROM Core.Fit_Calories c
LEFT JOIN (SELECT ServingsLast, Last, Food FROM Core.Fit_Calories WHERE Last=CURDATE()) AS sla ON sla.Last = c.Last AND sla.Food = c.Food
LEFT JOIN (SELECT ServingsLastE, LastE, Food FROM Core.Fit_Calories WHERE LastE=CURDATE()) AS sle ON sle.LastE = c.LastE AND sle.Food = c.Food;
