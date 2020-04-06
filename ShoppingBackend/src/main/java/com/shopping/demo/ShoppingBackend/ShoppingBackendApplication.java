package com.shopping.demo.ShoppingBackend;

import com.shopping.demo.ShoppingBackend.data.db.Item;
import com.shopping.demo.ShoppingBackend.repository.CartRepository;
import com.shopping.demo.ShoppingBackend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingBackendApplication  implements CommandLineRunner {

	@Autowired
	private ItemRepository repository;

	@Autowired
	private CartRepository cartRepository;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();
		cartRepository.deleteAll();;
		setupDBWithData();

		System.out.println("Items found with findAll():");
		System.out.println("-------------------------------");
		for (Item item : repository.findAll()) {
			System.out.println(item);
		}
		System.out.println();

		// fetch an individual item
		System.out.println("Item found by lookup");
		System.out.println("--------------------------------");
		System.out.println(repository.findByName("Photo frame"));
	}

	private void setupDBWithData() {
		// save a couple of items
		repository.save(new Item("123",
				"Eggs",
				"A dozen eggs. Plain and simple. A versatile weapon in the kitchen.",
				1299,
				"/images/eggs.jpg"
				));
		repository.save(new Item("456",
				"Hand Sanitizer",
				"A squirt a day keeps the COVID-19 away. Use sparingly, it's in short supply",
				599,
				"/images/handSanitizer.jpg"
		));
		repository.save(new Item("789",
				"Mask",
				"A simple, life-saving piece of equipment. Please reserve for healthcare professionals.",
				799,
				"/images/mask.png"
		));

		repository.save(new Item("111",
				"Milk",
				"It comes from cows. It tastes delicious. 'nuff said.",
				399,
				"/images/milk.jpg"
		));

		repository.save(new Item("222",
				"Toilet Paper",
				"For takin' care of business. Not sure why it's in short supply",
				199,
				"/images/toiletpaper.jpg"
		));

		repository.save(new Item("333",
				"Frozen Vegetables",
				"Good for you, as a side, or on their own.",
				499,
				"/images/vegtables.jpg"
		));

		repository.save(new Item("444",
				"Case of Water",
				"H20? more like h-2-WHOA",
				499,
				"/images/water.jpg"
		));
	}
}
