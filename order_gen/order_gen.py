import random

workers = [
    "Cole",
    "Ilham",
    "Dinesh",
    "Nicholas",
    "Mohsin",
]

items = [
    ["Classic Milk Tea", 3.5],
    ["Honey Milk Tea", 3.75],
    ["Classic Coffee", 2.5],
    ["Coffee Milk Tea", 4.5],
    ["Ginger Milk Tea", 3.6],
    ["Classic Pearl Milk Tea", 3.7],
    ["Hokkaido Pearl Milk Tea", 4.0],
    ["Okinawa Pearl Milk Tea", 4.0],
    ["Thai Pearl Milk Tea", 4.0],
    ["Taro Pearl Milk Tea", 4.2],
    ["Mango Green Milk Tea", 4.25],
    ["Matcha Red Bean Milk Tea", 5],
    ["Classic Tea", 2.0],
    ["Wintermelon Tea", 2.5],
    ["Honey Tea", 2.5],
    ["Ginger Tea", 2.5],
    ["Mango Green Tea", 3.0],
    ["Wintermelon Lemonade", 3.5],
    ["Strawberry Tea", 3.5],
    ["Peach Tea with Aiyu Jelly", 3.75],
    ["Honey Lemonade with Aloe Vera", 3.5],
    ["Peach Kiwi Tea with Aiyu Jelly", 4.0],
    ["Kiwi Fruit Tea with Aiyu Jelly", 4.0],
    ["Mango & Passion Fruit Tea", 4.25],
    ["Tropical Fruit Tea", 4.5],
    ["Hawaii Fruit Tea with Aiyu Jelly", 4.5],
    ["Passion Fruit Orange and Grapefruit Tea", 4.75],
    ["Fresh Milk Tea", 3.75],
    ["Wintermelon with Fresh Milk", 4.0],
    ["Cocoa Lover with Fresh Milk", 4.25],
    ["Matcha with Fresh Milk", 4.5],
    ["Handmade Taro with Fresh Milk", 4.75],
    ["Oreo Ice Blended with Pearl", 4.5],
    ["Milk Tea Ice Blended with Pearl", 4.75],
    ["Taro Ice Blended with Pudding", 4.75],
    ["Thai Tea Ice Blended with Pearl", 5.0],
    ["Matcha Red Bean Ice Blended with Ice Cream", 5.25],
    ["Coffee Ice Blended with Ice Cream", 5.0],
    ["Mango Ice Blended with Ice Cream", 5.25],
    ["Strawberry Ice Blended with Lychee Jelly & Ice Cream", 5.5],
    ["Peach Tea Ice Blended with Lychee Jelly", 5.0],
    ["Lime Mojito", 4.0],
    ["Mango Mojito", 4.25],
    ["Peach Mojito", 4.5],
    ["Strawberry Mojito", 4.75],
    ["Creama Tea (Black)", 4.0],
    ["Wintermelon Creama", 4.5],
    ["Coffee Creama", 4.75],
    ["Cocoa Creama", 4.75],
    ["Mango Creama", 5.0],
    ["Matcha Creama", 5.25],
]

item_popularity = [
    20,  # Classic Milk Tea
    17,  # Honey Milk Tea
    14,  # Classic Coffee
    17,  # Coffee Milk Tea
    13,  # Ginger Milk Tea
    16,  # Classic Pearl Milk Tea
    13,  # Hokkaido Pearl Milk Tea
    13,  # Okinawa Pearl Milk Tea
    17,  # Thai Pearl Milk Tea
    14,  # Taro Pearl Milk Tea
    14,  # Mango Green Milk Tea
    10,  # Matcha Red Bean Milk Tea
    13,  # Classic Tea
    8,  # Wintermelon Tea
    10,  # Honey Tea
    10,  # Ginger Tea
    12,  # Mango Green Tea
    9,  # Wintermelon Lemonade
    9,  # Strawberry Tea
    6,  # Peach Tea with Aiyu Jelly
    5,  # Honey Lemonade with Aloe Vera
    7,  # Peach Kiwi Tea with Aiyu Jelly
    6,  # Kiwi Fruit Tea with Aiyu Jelly
    9,  # Mango & Passion Fruit Tea
    11,  # Tropical Fruit Tea
    4,  # Hawaii Fruit Tea with Aiyu Jelly
    8,  # Passion Fruit Orange and Grapefruit Tea
    12,  # Fresh Milk Tea
    10,  # Wintermelon with Fresh Milk
    8,  # Cocoa Lover with Fresh Milk
    13,  # Matcha with Fresh Milk
    8,  # Handmade Taro with Fresh Milk
    7,  # Oreo Ice Blended with Pearl
    6,  # Milk Tea Ice Blended with Pearl
    3,  # Taro Ice Blended with Pudding
    9,  # Thai Tea Ice Blended with Pearl
    5,  # Matcha Red Bean Ice Blended with Ice Cream
    3,  # Coffee Ice Blended with Ice Cream
    4,  # Mango Ice Blended with Ice Cream
    3,  # Strawberry Ice Blended with Lychee Jelly & Ice Cream
    3,  # Peach Tea Ice Blended with Lychee Jelly
    8,  # Lime Mojito
    10,  # Mango Mojito
    9,  # Peach Mojito
    11,  # Strawberry Mojito
    9,  # Creama Tea (Black)
    8,  # Wintermelon Creama
    11,  # Coffee Creama
    12,  # Cocoa Creama
    13,  # Mango Creama
    12,  # Matcha Creama
]
item_popularity_total = sum(item_popularity)

toppings = [
    ["Pearl", 0.50],
    ["Aloe Vera", 0.60],
    ["Lychee Jelly", 0.60],
    ["Mini Pearl", 0.50],
    ["Ice Cream", 1.00],
    ["Herb Jelly", 0.50],
    ["Crystal Boba", 0.60],
    ["Pudding", 0.60],
    ["Aiyu Jelly", 0.60],
    ["Red Bean", 0.75],
]

topping_popularity = [
    7,  # Pearl
    1,  # Aloe Vera
    1,  # Lychee Jelly
    3,  # Mini Pearl
    1,  # Ice Cream
    1,  # Herb Jelly
    3,  # Crystal Boba
    1,  # Pudding
    1,  # Aiyu Jelly
    1,  # Red Bean
]
topping_popularity_total = sum(topping_popularity)

days = [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
]

day_popularity = [
    16,  # Sunday
    10,  # Monday
    12,  # Tuesday
    13,  # Wednesday
    15,  # Thursday
    19,  # Friday
    18,  # Saturday
]
day_popularity_total = sum(day_popularity)

topping_chance = 0.4
additional_item_chance = 0.1
order_id = 0


# Order format:
# [
#   id,
#   timestamp,
#   [
#   [item names],
#   ...,
#   items price,    ],
#   [
#   [toppings (names)],
#   [toppings2 (names)],
#   ...
#   toppings price  ],
#   worker (name)   ]
def generate_order():
    global order_id
    order = [order_id]
    order_id += 1
    minutes = str(random.randint(0, 59))
    timestamp = str(random.randint(11, 22)) + ":" + minutes.zfill(2)
    order.append(timestamp)
    workernum = random.randint(0, len(workers) - 1)

    num_items = 0
    item_tot = 0
    top_tot = 0
    while True:
        ordernum = random.randint(1, item_popularity_total)
        for i in range(len(item_popularity)):
            ordernum -= item_popularity[i]
            if ordernum <= 0:
                if num_items == 0:
                    order.append([[]])
                    order[2][0].append(items[i][0])
                else:
                    order[2][0].append(items[i][0])
                item_tot += items[i][1]
                break
        # Toppings are optional, so we need to check if we should add one or not
        # Create temporary copies of the toppings and their popularity
        top = toppings.copy()
        pop = topping_popularity.copy()
        pop_tot = sum(pop)
        top_chance = topping_chance
        if num_items == 0:
            order.append([[]])
        else:
            order[3].append([])
        while random.random() < top_chance and len(top) > 0:
            toppingnum = random.randint(1, pop_tot)
            for i in range(len(pop)):
                toppingnum -= pop[i]
                if toppingnum <= 0:
                    order[3][num_items].append(top[i][0])
                    top_tot += top[i][1]
                    top.pop(i)
                    pop.pop(i)
                    break
            pop_tot = sum(pop)
            top_chance *= 0.75

        order[3][num_items].sort()
        if len(order[3][num_items]) == 0:
            order[3][num_items].append("None")
        num_items += 1
        if random.random() > 0.1:
            break
    order[2].append(item_tot)
    order[3].append(top_tot)
    order.append(workers[workernum])
    return order


def order_string(order):
    string = str(order[0]) + ", " + order[1] + ", "

    for i in range(0, len(order[2][0])):
        if i == len(order[2][0]) - 1:
            string += order[2][0][i] + ", "
        else:
            string += order[2][0][i] + "; "
    string += str(order[2][1]) + ", "
    for i in range(0, len(order[3])):
        if i < len(order[3]) - 1:
            for j in range(0, len(order[3][i])):
                if i == len(order[3]) - 2 and j == len(order[3][i]) - 1:
                    string += order[3][i][j] + ", "
                else:
                    string += order[3][i][j] + "; "
    string += str(order[3][-1])
    string += ", " + order[4]

    return string


weekly_total = 19300


def generate_day(week, weekday):
    orders = []
    day = [week, weekday, orders]
    target = (
        weekly_total * day_popularity[weekday] / day_popularity_total
        + (random.random() - 0.5) * 500
    )
    if weekday == 5 and week == 3:
        target *= 1.75
    elif weekday == 5 and week == 34:
        target *= 2
    daily_earnings = 0
    while daily_earnings < target:
        order = generate_order()
        orders.append(order)
        daily_earnings += order[2][-1]
        daily_earnings += order[3][-1]
    day.append(daily_earnings)
    return day


def generate_week(week):
    week = [
        generate_day(week, 0),
        generate_day(week, 1),
        generate_day(week, 2),
        generate_day(week, 3),
        generate_day(week, 4),
        generate_day(week, 5),
        generate_day(week, 6),
    ]
    return week


if __name__ == "__main__":
    year = []
    for i in range(1, 53):
        year.append(generate_week(i))
    results = open("orders.csv", "w")
    results.write(
        "Week, Day, Order ID, Timestamp, Item(s), Items' Price, Topping(s), Topping Price, Worker\n"
    )
    for week in year:
        for day in week:
            for order in day[2]:
                results.write(
                    str(day[0])
                    + ", "
                    + days[day[1]]
                    + ", "
                    + order_string(order)
                    + "\n"
                )
