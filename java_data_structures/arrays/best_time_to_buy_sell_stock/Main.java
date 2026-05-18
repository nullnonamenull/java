import java.util.List;

class Main {

	public static void main(String[] args) {
		var prices = List.of(7, 1, 5, 3, 6, 4);
		// var prices = List.of(7, 6, 4, 3, 1);

		var bestProfit = bestProfit(prices);
		System.out.println(bestProfit);
	}

	static int bestProfit(List<Integer> prices) {
		var minPrice = Integer.MAX_VALUE;
		var bestProfit = 0;
		for (int i = 0; i < prices.size(); i++) {
			var price = prices.get(i);
			if (minPrice > price) {
				minPrice = price;
			}

			var profit = price - minPrice;
			if (profit > bestProfit) {
				bestProfit = profit;
			}
		}

		return bestProfit;
	}

}