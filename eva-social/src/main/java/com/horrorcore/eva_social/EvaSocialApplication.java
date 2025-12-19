package com.horrorcore.eva_social;

import com.horrorcore.eva_social.entites.Comment;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.repositories.CommentRepository;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class EvaSocialApplication implements CommandLineRunner {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostService postService;

	public static void main(String[] args) {
		SpringApplication.run(EvaSocialApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create and save posts first
		Post post1 = new Post();
		post1.setStatus("Just started learning Spring Boot! Excited to build my first REST API.");
		post1.setLikes(15);
		post1.setDislikes(1);
		post1.setCreatedAt(LocalDateTime.now().minusDays(5));
		post1.setUpdatedAt(LocalDateTime.now().minusDays(5));

		Post post2 = new Post();
		post2.setStatus("Had an amazing debugging session today. Finally found that pesky null pointer exception!");
		post2.setLikes(42);
		post2.setDislikes(3);
		post2.setCreatedAt(LocalDateTime.now().minusDays(3));
		post2.setUpdatedAt(LocalDateTime.now().minusDays(3));

		Post post3 = new Post();
		post3.setStatus("Coffee + Code = Productivity. Who else agrees?");
		post3.setLikes(89);
		post3.setDislikes(5);
		post3.setCreatedAt(LocalDateTime.now().minusDays(2));
		post3.setUpdatedAt(LocalDateTime.now().minusDays(1));

		Post post4 = new Post();
		post4.setStatus("Deployed my first application to production today! Feeling accomplished.");
		post4.setLikes(127);
		post4.setDislikes(2);
		post4.setCreatedAt(LocalDateTime.now().minusDays(1));
		post4.setUpdatedAt(LocalDateTime.now().minusDays(1));

		Post post5 = new Post();
		post5.setStatus("Anyone else think documentation is just as important as writing code?");
		post5.setLikes(63);
		post5.setDislikes(8);
		post5.setCreatedAt(LocalDateTime.now().minusHours(12));
		post5.setUpdatedAt(LocalDateTime.now().minusHours(12));

		postRepository.saveAll(List.of(post1, post2, post3, post4, post5));

		// Create comments for post1
		Comment comment1 = new Comment();
		comment1.setMessage("Great start! Spring Boot is amazing for building APIs quickly.");
		comment1.setLikes(8);
		comment1.setDislikes(0);
		comment1.setCreatedAt(LocalDateTime.now().minusDays(4));
		comment1.setUpdatedAt(LocalDateTime.now().minusDays(4));

		Comment comment2 = new Comment();
		comment2.setMessage("I remember my first Spring Boot project. Keep going!");
		comment2.setLikes(5);
		comment2.setDislikes(0);
		comment2.setCreatedAt(LocalDateTime.now().minusDays(4).minusHours(3));
		comment2.setUpdatedAt(LocalDateTime.now().minusDays(4).minusHours(3));

		// Create comments for post2
		Comment comment3 = new Comment();
		comment3.setMessage("Debugging is an art. Well done on solving it!");
		comment3.setLikes(12);
		comment3.setDislikes(1);
		comment3.setCreatedAt(LocalDateTime.now().minusDays(2));
		comment3.setUpdatedAt(LocalDateTime.now().minusDays(2));

		Comment nestedComment1 = new Comment();
		nestedComment1.setMessage("Totally agree! Sometimes I spend hours on one bug.");
		nestedComment1.setLikes(7);
		nestedComment1.setDislikes(0);
		nestedComment1.setCreatedAt(LocalDateTime.now().minusDays(2).minusHours(6));
		nestedComment1.setUpdatedAt(LocalDateTime.now().minusDays(2).minusHours(6));

		Comment nestedComment2 = new Comment();
		nestedComment2.setMessage("Pro tip: rubber duck debugging helps!");
		nestedComment2.setLikes(15);
		nestedComment2.setDislikes(0);
		nestedComment2.setCreatedAt(LocalDateTime.now().minusDays(2).minusHours(3));
		nestedComment2.setUpdatedAt(LocalDateTime.now().minusDays(2).minusHours(3));

		commentRepository.saveAll(List.of(nestedComment1, nestedComment2));
		comment3.setComments(List.of(nestedComment1, nestedComment2));

		// Create comments for post3
		Comment comment4 = new Comment();
		comment4.setMessage("Coffee is the developer's fuel!");
		comment4.setLikes(22);
		comment4.setDislikes(2);
		comment4.setCreatedAt(LocalDateTime.now().minusDays(1));
		comment4.setUpdatedAt(LocalDateTime.now().minusDays(1));

		// Create comments for post4
		Comment comment5 = new Comment();
		comment5.setMessage("Congrats! The first production deploy is always special.");
		comment5.setLikes(18);
		comment5.setDislikes(0);
		comment5.setCreatedAt(LocalDateTime.now().minusHours(20));
		comment5.setUpdatedAt(LocalDateTime.now().minusHours(20));

		Comment nestedComment3 = new Comment();
		nestedComment3.setMessage("Did you use CI/CD pipelines?");
		nestedComment3.setLikes(4);
		nestedComment3.setDislikes(0);
		nestedComment3.setCreatedAt(LocalDateTime.now().minusHours(18));
		nestedComment3.setUpdatedAt(LocalDateTime.now().minusHours(18));

		commentRepository.save(nestedComment3);
		comment5.setComments(List.of(nestedComment3));

		// Create comments for post5
		Comment comment6 = new Comment();
		comment6.setMessage("Documentation saves lives! Future you will thank you.");
		comment6.setLikes(31);
		comment6.setDislikes(1);
		comment6.setCreatedAt(LocalDateTime.now().minusHours(10));
		comment6.setUpdatedAt(LocalDateTime.now().minusHours(10));

		Comment comment7 = new Comment();
		comment7.setMessage("Yes! Good docs are worth their weight in gold.");
		comment7.setLikes(14);
		comment7.setDislikes(0);
		comment7.setCreatedAt(LocalDateTime.now().minusHours(8));
		comment7.setUpdatedAt(LocalDateTime.now().minusHours(8));

		// Save all comments
		commentRepository.saveAll(List.of(comment1, comment2, comment3, comment4, comment5, comment6, comment7));

		// Link comments to posts
		post1.setComments(List.of(comment1, comment2));
		post2.setComments(List.of(comment3));
		post3.setComments(List.of(comment4));
		post4.setComments(List.of(comment5));
		post5.setComments(List.of(comment6, comment7));

		// Save posts again to persist relationships
		postRepository.saveAll(List.of(post1, post2, post3, post4, post5));

		System.out.println("Dummy posts and comments created successfully!");
		postService.getAllPosts().forEach(System.out::println);
	}

}
