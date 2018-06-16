package net.netnook.repeg.examples.template.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HouseRepository {

	private final List<House> houses;

	public HouseRepository() {
		houses = buildHouses();
	}

	public List<House> getHouses() {
		return houses;
	}

	private List<House> buildHouses() {
		List<House> houses = new ArrayList<>();

		{
			House house = new House();
			houses.add(house);
			house.setAddress(new Address());
			house.getAddress().setStreetAndNumber("Rosenheimer Str. 2635");
			house.getAddress().setCity("Munich");
			house.getAddress().setPostcode("81667");
			house.setArea(78);
			house.setNumberOfRooms(3);
			house.setMonthlyRent(new BigDecimal("810.00"));
			house.setFurnished(true);
		}

		{
			House house = new House();
			houses.add(house);
			house.setAddress(new Address());
			house.getAddress().setStreetAndNumber("Bahnhofstr. 17");
			house.getAddress().setCity("Munich");
			house.getAddress().setPostcode("83559");
			house.setArea(152);
			house.setNumberOfRooms(5);
			house.setMonthlyRent(new BigDecimal("2105.00"));
			house.setFurnished(false);
		}

		return houses;
	}
}
