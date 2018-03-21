SELECT Food, Serving, Calories, Carbs, Protein, Sugar,
	Sodium, Fat, Cholest, Fiber, Water, FruitVeggie, Last, ServingsLast, ServingsLastE, LastE,
	(SELECT ServingsLast FROM Core.Fit_Calories WHERE Last=CURDATE() AND Food=Food LIMIT 1) as ThisServingsLast,
	(SELECT ServingsLastE FROM Core.Fit_Calories WHERE Last=CURDATE() AND Food=Food LIMIT 1) as ThisServingsLastE
FROM Core.Fit_Calories;
