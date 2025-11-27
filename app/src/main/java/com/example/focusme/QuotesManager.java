package com.example.focusme;

import java.util.Random;

public class QuotesManager {
    private static final String[] MOTIVATIONAL_QUOTES = {
            "“The future depends on what you do today.” - Mahatma Gandhi",
            "“Don't watch the clock; do what it does. Keep going.” - Sam Levenson",
            "“The way to get started is to quit talking and begin doing.” - Walt Disney",
            "“It always seems impossible until it's done.” - Nelson Mandela",
            "“You are never too old to set another goal or to dream a new dream.” - C.S. Lewis",
            "“The only way to do great work is to love what you do.” - Steve Jobs",
            "“Believe you can and you're halfway there.” - Theodore Roosevelt",
            "“Your time is limited, don't waste it living someone else's life.” - Steve Jobs",
            "“The harder you work for something, the greater you'll feel when you achieve it.”",
            "“Dream bigger. Do bigger.”",
            "“Stay focused, go after your dreams and keep moving toward your goals.”",
            "“Success doesn't just find you. You have to go out and get it.”",
            "“The key to success is to focus on goals, not obstacles.”",
            "“Don't stop when you're tired. Stop when you're done.”",
            "“Great things never come from comfort zones.”",
            "“The pain you feel today will be the strength you feel tomorrow.”",
            "“Push yourself, because no one else is going to do it for you.”",
            "“The only limit to our realization of tomorrow will be our doubts of today.” - Franklin D. Roosevelt",
            "“It does not matter how slowly you go as long as you do not stop.” - Confucius",
            "“Everything you've ever wanted is on the other side of fear.” - George Addair"
    };

    private static final String[] BREAK_QUOTES = {
            "“Take a break! Your brain needs to recharge.”",
            "“Rest when you're weary. Refresh and renew yourself, your body, your mind, your spirit.” - Ralph Marston",
            "“Almost everything will work again if you unplug it for a few minutes, including you.” - Anne Lamott",
            "“Taking a break can lead to breakthroughs.” - Russell Eric Dobda",
            "“The time to relax is when you don't have time for it.” - Sydney J. Harris",
            "“Rest is not idleness, and to lie sometimes on the grass under trees on a summer's day is by no means a waste of time.” - John Lubbock",
            "“A good rest is half the work.”",
            "“Your mind will answer most questions if you learn to relax and wait for the answer.” - William S. Burroughs",
            "“Sometimes the most productive thing you can do is relax.” - Mark Black",
            "“Breaks are not a sign of weakness but a tool for strength.”"
    };

    private static Random random = new Random();
    public static String getRandomMotivationalQuote() {
        return MOTIVATIONAL_QUOTES[random.nextInt(MOTIVATIONAL_QUOTES.length)];
    }
    public static String getRandomBreakQuote() {
        return BREAK_QUOTES[random.nextInt(BREAK_QUOTES.length)];
    }
}