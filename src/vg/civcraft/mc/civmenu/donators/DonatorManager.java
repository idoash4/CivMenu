package vg.civcraft.mc.civmenu.donators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class DonatorManager {

	private List<Donator> donators;
	private Random rng;

	private class Donator {
		public String name;
		public int initialValue;
		public double relativeChance;

		private Donator(String name, int initialValue, double relativeChance) {
			this.name = name;
			this.initialValue = initialValue;
			this.relativeChance = relativeChance;
		}

		public String getName() {
			return name;
		}

		public int getInitialValue() {
			return initialValue;
		}

		public double getRelativeChance() {
			return relativeChance;
		}
	}

	public DonatorManager(Map<String, Integer> relativeValue) {
		recalculateDonators(relativeValue);
		rng = new Random();
	}

	public void recalculateDonators(Map<String, Integer> relativeValue) {
		donators = new ArrayList<DonatorManager.Donator>();
		// calculate sum of given values
		int sum = 0;
		for (int val : relativeValue.values()) {
			sum += val;
		}
		// calculate specific chance depending on portion of sum
		for (Entry<String, Integer> entry : relativeValue.entrySet()) {
			double chance = ((double) entry.getValue()) / ((double) sum);
			Donator don = new Donator(entry.getKey(), entry.getValue(), chance);
			donators.add(don);
		}

	}

	public String getRandomPlayerName() {
		double roll = rng.nextDouble();
		double sum = 0.0;
		for (Donator donator : donators) {
			sum += donator.getRelativeChance();
			if (sum >= roll) {
				return donator.getName();
			}
		}
		return "ttk2"; // this should never happen
	}

	public List<String> getRandomPlayerName(int amount) {
		List<String> result = new LinkedList<String>();
		for (int i = 0; i < amount; i++) {
			result.add(getRandomPlayerName());
		}
		return result;
	}

}
