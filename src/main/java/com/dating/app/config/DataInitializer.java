package com.dating.app.config;

import com.dating.app.model.Gender;
import com.dating.app.model.Like;
import com.dating.app.model.Profile;
import com.dating.app.repository.LikeRepository;
import com.dating.app.repository.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final LikeRepository likeRepository;

    public DataInitializer(ProfileRepository profileRepository, LikeRepository likeRepository) {
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public void run(String... args) {
        if (profileRepository.count() > 0) return;

        List<Profile> males = List.of(
            new Profile("James Carter", 28, Gender.MALE,
                "Adventure lover and weekend hiker. I make a mean homemade pasta and I'm always up for trying new restaurants. Looking for someone to share life's little moments with.",
                "Hiking, Cooking, Photography, Travel, Jazz Music",
                "New York, NY", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/1.jpg"),

            new Profile("Liam Nguyen", 31, Gender.MALE,
                "Software engineer by day, amateur guitarist by night. I believe good coffee and good conversation can fix almost anything. Dog dad to a golden retriever named Biscuit.",
                "Guitar, Coffee, Coding, Dogs, Cycling",
                "San Francisco, CA", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/2.jpg"),

            new Profile("Marcus Thompson", 26, Gender.MALE,
                "Aspiring chef who loves experimenting with global cuisines. Fitness enthusiast who hits the gym 5 days a week. Looking for someone spontaneous and genuine.",
                "Cooking, Gym, Salsa Dancing, Travelling, Reading",
                "Miami, FL", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/3.jpg"),

            new Profile("Oliver Bennett", 34, Gender.MALE,
                "Writer working on my first novel. I spend most evenings at cozy bookshops or independent cinemas. Looking for someone who appreciates the slower, richer things in life.",
                "Writing, Cinema, Books, Wine, Philosophy",
                "Portland, OR", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/4.jpg"),

            new Profile("Ethan Rivera", 29, Gender.MALE,
                "Marine biologist passionate about ocean conservation. I free dive, kayak, and can identify every fish at the aquarium. Looking for someone adventurous who loves the sea.",
                "Scuba Diving, Kayaking, Marine Biology, Surfing, Yoga",
                "San Diego, CA", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/5.jpg"),

            new Profile("Noah Williams", 27, Gender.MALE,
                "Graphic designer with an obsession for typography and street art. I love vinyl records, rooftop sunsets, and spontaneous road trips. Probably funnier in person.",
                "Design, Street Art, Vinyl Records, Road Trips, Basketball",
                "Los Angeles, CA", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/6.jpg"),

            new Profile("Sebastian Park", 33, Gender.MALE,
                "Financial analyst who unwinds by rock climbing and playing chess online. I'm a huge foodie — always hunting for the best ramen in town. Low-key but genuinely caring.",
                "Rock Climbing, Chess, Finance, Ramen, Podcasts",
                "Chicago, IL", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/7.jpg"),

            new Profile("Daniel Moore", 30, Gender.MALE,
                "Pediatric nurse who volunteers at animal shelters on weekends. I'm a sucker for cheesy rom-coms and competitive board games. Life's too short to be boring.",
                "Volunteering, Board Games, Movies, Cycling, Cooking",
                "Austin, TX", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/8.jpg"),

            new Profile("Ryan Chen", 25, Gender.MALE,
                "Architecture student with a love for sustainable design. I sketch city skylines, brew my own kombucha, and explore abandoned buildings (legally). Very open-minded.",
                "Architecture, Sketching, Kombucha Brewing, Urban Exploration, Music",
                "Seattle, WA", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/9.jpg"),

            new Profile("Alex Johnson", 36, Gender.MALE,
                "High school history teacher who coaches the debate team. I travel every summer to explore ancient ruins. Passionate about politics, tacos, and good podcasts.",
                "History, Travel, Debate, Tacos, Podcasts",
                "Denver, CO", Gender.FEMALE,
                "https://randomuser.me/api/portraits/men/10.jpg")
        );

        List<Profile> females = List.of(
            new Profile("Sophia Martinez", 27, Gender.FEMALE,
                "Event planner who brings joy to every celebration. I love rooftop yoga, vintage thrift shopping, and making homemade candles. Looking for someone genuine and kind-hearted.",
                "Yoga, Thrift Shopping, Candle Making, Event Planning, Dancing",
                "New York, NY", Gender.MALE,
                "https://randomuser.me/api/portraits/women/1.jpg"),

            new Profile("Olivia Kim", 30, Gender.FEMALE,
                "UX designer passionate about accessible technology. I recharge with trail running and watercolour painting on rainy Sundays. Huge fan of board games and bad puns.",
                "UX Design, Trail Running, Watercolour, Board Games, Baking",
                "San Francisco, CA", Gender.MALE,
                "https://randomuser.me/api/portraits/women/2.jpg"),

            new Profile("Amara Jackson", 28, Gender.FEMALE,
                "Environmental lawyer fighting for climate justice by day, salsa dancer by night. I grow my own herbs and bake sourdough every weekend. Passionate and a little intense.",
                "Law, Salsa Dancing, Gardening, Baking, Activism",
                "Washington, DC", Gender.MALE,
                "https://randomuser.me/api/portraits/women/3.jpg"),

            new Profile("Isabella Rossi", 32, Gender.FEMALE,
                "Italian-American food blogger and amateur sommelier. I host dinner parties with too many courses and not enough chairs. Looking for someone who appreciates a good meal.",
                "Food Blogging, Wine, Cooking, Travel, Photography",
                "Boston, MA", Gender.MALE,
                "https://randomuser.me/api/portraits/women/4.jpg"),

            new Profile("Zoe Campbell", 26, Gender.FEMALE,
                "Kindergarten teacher by day, stand-up comedy fan by night. I believe laughter is everything. I'm crafty, spontaneous, and unbeatable at mini golf.",
                "Teaching, Comedy, Crafts, Mini Golf, Hiking",
                "Nashville, TN", Gender.MALE,
                "https://randomuser.me/api/portraits/women/5.jpg"),

            new Profile("Priya Patel", 29, Gender.FEMALE,
                "Data scientist with a love for statistical storytelling. I run half-marathons, practice Bharatanatyam dance, and make incredible chai from scratch. Nerdy but fun.",
                "Data Science, Running, Classical Dance, Chai, Reading",
                "Houston, TX", Gender.MALE,
                "https://randomuser.me/api/portraits/women/6.jpg"),

            new Profile("Charlotte Davis", 31, Gender.FEMALE,
                "Architect designing green spaces for urban communities. I kayak every Sunday, collect succulents, and have strong opinions about typefaces. Thoughtful and adventurous.",
                "Architecture, Kayaking, Succulents, Design, Climbing",
                "Seattle, WA", Gender.MALE,
                "https://randomuser.me/api/portraits/women/7.jpg"),

            new Profile("Maya Robinson", 24, Gender.FEMALE,
                "Aspiring musician studying jazz composition. I perform at open mics, make my own jewellery, and stay up too late watching documentaries. Big heart, bigger laugh.",
                "Music, Jazz, Jewellery Making, Documentaries, Cycling",
                "New Orleans, LA", Gender.MALE,
                "https://randomuser.me/api/portraits/women/8.jpg"),

            new Profile("Hannah Lee", 33, Gender.FEMALE,
                "Cardiologist who unwinds with pottery and long trail walks. I travel solo twice a year and speak three languages. Looking for depth, honesty, and someone who can keep up.",
                "Medicine, Pottery, Hiking, Languages, Solo Travel",
                "San Diego, CA", Gender.MALE,
                "https://randomuser.me/api/portraits/women/9.jpg"),

            new Profile("Grace Wilson", 35, Gender.FEMALE,
                "Freelance journalist covering human interest stories around the world. I'm obsessed with strong espresso, foreign films, and vintage maps. Looking for someone real.",
                "Journalism, Travel, Espresso, Foreign Films, Vintage Maps",
                "Chicago, IL", Gender.MALE,
                "https://randomuser.me/api/portraits/women/10.jpg")
        );

        List<Profile> savedMales = profileRepository.saveAll(males);
        List<Profile> savedFemales = profileRepository.saveAll(females);

        // Seed some mutual likes to create example matches
        likeRepository.save(new Like(savedMales.get(0).getId(), savedFemales.get(0).getId()));
        likeRepository.save(new Like(savedFemales.get(0).getId(), savedMales.get(0).getId()));

        likeRepository.save(new Like(savedMales.get(1).getId(), savedFemales.get(1).getId()));
        likeRepository.save(new Like(savedFemales.get(1).getId(), savedMales.get(1).getId()));

        likeRepository.save(new Like(savedMales.get(4).getId(), savedFemales.get(8).getId()));
        likeRepository.save(new Like(savedFemales.get(8).getId(), savedMales.get(4).getId()));

        // One-sided likes (no match yet)
        likeRepository.save(new Like(savedMales.get(2).getId(), savedFemales.get(2).getId()));
        likeRepository.save(new Like(savedMales.get(3).getId(), savedFemales.get(3).getId()));
        likeRepository.save(new Like(savedFemales.get(4).getId(), savedMales.get(5).getId()));
        likeRepository.save(new Like(savedFemales.get(6).getId(), savedMales.get(6).getId()));
    }
}
