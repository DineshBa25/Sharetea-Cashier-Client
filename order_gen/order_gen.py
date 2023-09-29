import random

workers = [
    "Cole",
    "Ilham",
    "Dinesh",
    "Nicholas",
    "Mohsin",
]

items = [
    ["Classic Milk Tea", 5],
    ["Honey Milk Tea", 5.50],
    ["Classic Coffee", 3.50],
    ["Coffee Milk Tea", 6],
    ["Ginger Milk Tea", 5.50],
    ["Classic Pearl Milk Tea", 6.50],
    ["Hokkaido Pearl Milk Tea", 6.50],
    ["Okinawa Pearl Milk Tea", 6.50],
    ["Thai Pearl Milk Tea", 7],
    ["Taro Pearl Milk Tea", 7],
    ["Mango Green Milk Tea", 6.50],
    ["Matcha Red Bean Milk Tea", 6.50],
    ["Mango Green Tea", 5.50],
    ["Wintermelon Lemonade", 4],
    ["Strawberry Tea", 5.50],
    ["Tropical Fruit Tea", 5.50],
    ["Fresh Milk Tea", 5.50],
    ["Oreo Ice Blended with Pearl", 7.50],
    ["Coffee Ice Blended with Ice Cream", 5.50],
    ["Mango Ice Blended with Ice Cream", 5.50],
    ["Passion Fruit Orange and Grapefruit Tea", 6],
]

item_popularity = [
    18, # Classic Milk Tea
    10, # Honey Milk Tea
    7, # Classic Coffee
    11, # Coffee Milk Tea
    9, # Ginger Milk Tea
    17, # Classic Pearl Milk Tea
    8, # Hokkaido Pearl Milk Tea
    7, # Okinawa Pearl Milk Tea
    15, # Thai Pearl Milk Tea
    8, # Taro Pearl Milk Tea
    11, # Mango Green Milk Tea
    6, # Matcha Red Bean Milk Tea
    12, # Mango Green Tea
    5, # Wintermelon Lemonade
    11, # Strawberry Tea
    10, # Tropical Fruit Tea
    12, # Fresh Milk Tea
    12, # Oreo Ice Blended with Pearl
    6, # Coffee Ice Blended with Ice Cream
    7, # Mango Ice Blended with Ice Cream
    8, # Passion Fruit, Orange and Grapefruit Tea
]
item_popularity_total = sum(item_popularity)

toppings = [
    ["Pearl", 0.75],
    ["Mini Pearl", 0.75],
    ["Ice Cream", 1],
    ["Pudding", 1],
    ["Aloe Vera", 0.75],
    ["Red Bean", 0.75],
    ["Herb Jelly", 0.75],
    ["Aiyu Jelly", 0.75],
    ["Lychee Jelly", 0.64],
]

topping_popularity = [
    5, # Pearl
    1, # Mini Pearl
    1, # Ice Cream
    1, # Pudding
    1, # Aloe Vera
    1, # Red Bean
    1, # Herb Jelly
    1, # Aiyu Jelly
    1, # Lychee Jelly
]
topping_popularity_total = sum(topping_popularity)

days = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
]

day_popularity = [
    10, # Monday
    12, # Tuesday
    13, # Wednesday
    15, # Thursday
    19, # Friday
    18, # Saturday
    16, # Sunday
]
day_popularity_total = sum(day_popularity)

topping_chance = 0.4
order_id = 0
# Order format: [id, item (name, price), topping (name, price), worker (name)]
def generate_order():
    global order_id
    order = [order_id]
    order_id += 1
    ordernum = random.randint(1, item_popularity_total)
    toppingnum = random.randint(1, topping_popularity_total)
    workernum = random.randint(0, len(workers) - 1)
    for i in range(len(item_popularity)):
        ordernum -= item_popularity[i]
        if ordernum <= 0:
            order.append(items[i])
            break
    for i in range(len(topping_popularity)):
        toppingnum -= topping_popularity[i]
        if toppingnum <= 0:
            if random.random() < topping_chance:
                order.append(toppings[i])
            break
    if len(order) == 2:
        order.append(["None", 0])
    order.append(workers[workernum])
    return order

weekly_total = 19300

def generate_day(week, weekday):
    orders = []
    day = [week, weekday, orders]
    target = weekly_total * day_popularity[weekday] / day_popularity_total + (random.random() - 0.5) * 500
    if (weekday == 5 and week == 3):
        target *= 1.75
    elif (weekday == 5 and week == 34):
        target *= 2
    daily_earnings = 0
    while daily_earnings < target:
        order = generate_order()
        orders.append(order)
        daily_earnings += order[1][1]
        if order[2][1] != "None":
            daily_earnings += order[2][1]
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
    for i in range(1, 52):
        year.append(generate_week(i))
    results = open("orders.csv", "w")
    results.write("Week, Day, Order ID, Item, Item Price, Topping, Topping Price, Worker\n")
    for week in year:
        for day in week:
            for order in day[2]:
                results.write(str(day[0]) + ", " + days[day[1]] + ", " + str(order[0]) + ", " + order[1][0] + ", " + str(order[1][1]) + ", " + order[2][0] + ", " + str(order[2][1]) + ", " + order[3] + "\n")
        
    