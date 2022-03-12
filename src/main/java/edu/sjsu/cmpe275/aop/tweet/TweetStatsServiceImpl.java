package edu.sjsu.cmpe275.aop.tweet;

import java.util.*;

public class TweetStatsServiceImpl implements TweetStatsService {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */

	private int longestTweetLength = 0;
	private HashMap<String, List<String>> followersMap = new HashMap<>();
	private HashMap<String, List<String>> blockedMap = new HashMap<>();
	private HashMap<String, List<UUID>> likedMap = new HashMap<>();
	private HashMap<UUID, List<String>> UUIDMap = new HashMap<>();
	//private HashSet<UUID, List<String>> UUIDShareMap = new HashMap<>();
	private HashMap<String,Integer> followerCount = new HashMap<>();
	private HashMap<String,Integer> messageCount = new HashMap<>();
	private HashMap<UUID,List<String>> messageSharedToMap = new HashMap<>();
	private HashMap<UUID,UUID> replyMap = new HashMap<>();



	@Override
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getMostPopularMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getMostProductiveReplier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getMostLikedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMostUnpopularFollower() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getLongestMessageThread() {
		// TODO Auto-generated method stub
		return null;
	}

	public UUID tweet(String user, String message){
		return null;
	}


	/**
	 * This method allows a user to block a follower or a potential follower so that
	 * subsequently tweets will not be shared with the latter. The
	 * same block operation can be repeated.

	 * //@throws IllegalArgumentException if either parameter is null or empty, or
	 *                                  when a user attempts to block himself.
	 * //@throws IOException              if there is a network failure.
	 */

	public void follow( String follower, String followee){

		List<String> followers = followersMap.get(followee);
		Integer fCount = followerCount.get(followee);
		if (followers == null ) {
			followers = new ArrayList<String>();
			fCount = new Integer(0);
		}
		followers.add(follower);
		followersMap.put(followee, followers);
		followerCount.put(followee, new Integer(fCount.intValue() + 1));
		System.out.println("Follower added for " + followee);
	}

	/**
	 * This method allows a user to block a follower or a potential follower so that
	 * subsequently tweets will not be shared with the latter. The
	 * same block operation can be repeated.

	 * @throws IllegalArgumentException if either parameter is null or empty, or
	 *                                  when a user attempts to block himself.
	 * //@throws IOException              if there is a network failure.
	 */
	public void block(String user, String follower){
		List<String> blocked = blockedMap.get(user);
		if (blocked == null ){
			blocked = new ArrayList<String>();
		}
		blocked.add(follower);
		blockedMap.put(user, blocked);
		if (followersMap.get(user) != null && followersMap.get(user).contains(follower)){
			followersMap.get(user).remove(user);
		}
		//Update follower Count
		Integer fCount = followerCount.get(user);
		followerCount.put(user, new Integer(fCount.intValue() - 1));

	}


	public void like(String user, UUID messageId){
		//Check if writer of UUID is not user
		List<String> listMapped = UUIDMap.get(messageId);
		if (!listMapped.get(1).equals(user)){
			//Check if already liked
			List<UUID> likedTweets = likedMap.get(user);
			if (!likedTweets.contains(messageId)){
				likedTweets.add(messageId);
				likedMap.put(user, likedTweets);
		}


		}



	}

	public void addTweet(String user, String message, UUID uuid){
		int messageLength = message.length();
		if(messageLength > longestTweetLength){
			longestTweetLength = messageLength;
		}
		List<String> newList = new ArrayList<>();
		newList.add(user);
		newList.add(message);
		newList.add("T");
		UUIDMap.put(uuid,newList);

		//Update message Count
		Integer messCount = messageCount.get(user);
		if (messCount == null){
			messageCount.put(user,1);
		} else {
			messageCount.replace(user, messCount.intValue() + 1);
		}

		//Update messageShared to Map
		List<String> sharedTo = followersMap.get(user);

			//Handle if user not exist etc
		messageSharedToMap.put(uuid, sharedTo);

	}


	public void reply(String user, UUID originalMessage, String message, UUID newMessage){
		//Handle check if user follows etc etc
		//Update Tweet Mapping
		List<String> newList = new ArrayList<>();
		newList.add(user);
		newList.add(message);
		newList.add("R");
		UUIDMap.put(newMessage,newList);

		//Update Reply Mapping
		replyMap.put(originalMessage,newMessage);

		//Update sharedTo map
		List<String> sharedTo = followersMap.get(user);
		List<String>originalMessageList = UUIDMap.get(originalMessage);
		String originalUser = originalMessageList.get(1);
		sharedTo.add(originalUser);
		messageSharedToMap.put(newMessage, sharedTo);

	}



}



