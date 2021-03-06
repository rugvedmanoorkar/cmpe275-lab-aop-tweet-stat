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
	private Map<String, HashSet<String>> blockedByMap = new HashMap<>();
	private String maxBlockedUser = null;
	private Integer maxBlockedCount = 0;

	private HashMap<String, List<UUID>> likedMap = new HashMap<>();
	private HashMap<UUID, List<String>> UUIDMap = new HashMap<>();

	private HashMap<String,Integer> followerCount = new HashMap<>();
	private HashMap<String,Integer> messageCount = new HashMap<>();
	private HashMap<UUID,Integer> likeCount = new HashMap<>();
	private UUID maxLikedTweetID = null;
	private Integer maxLikedTweet = 0;

	private HashMap<UUID,List<String>> messageSharedToMap = new HashMap<>();
	private HashMap<UUID,UUID> replyMap = new HashMap<>();

	private Map<UUID, Integer> messageThreadPosition = new TreeMap<>();
	private Integer longestThreadLength = 0;
	private UUID longestThreadUUID = null;

	private String longestReplierName = null;
	private Integer longestReplier = 0;
	private Map<String, Integer> productivityCount = new TreeMap<>();


	@Override
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		longestTweetLength = 0;
		followersMap.clear();
		blockedMap.clear();
		likedMap.clear();
		UUIDMap.clear();
		followerCount.clear();
		messageCount.clear();
		messageSharedToMap.clear();
		replyMap.clear();

	}
    
	@Override
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		return longestTweetLength;
	}

	@Override
	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String , Integer> entry : followerCount.entrySet())
		{
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
			{
				maxEntry = entry;
			}
		}
		if(maxEntry != null){
			return maxEntry.getKey();
		} else {
			return null;
		}
	}

	@Override
	public UUID getMostPopularMessage() {
		// TODO Auto-generated method stub
		return mostSharedMessage();
	}
	
	@Override
	public String getMostProductiveReplier() {
		// TODO Auto-generated method stub
		return longestReplierName;
	}

	@Override
	public UUID getMostLikedMessage() {
		// TODO Auto-generated method stub
		return maxLikedTweetID;
	}

	@Override
	public String getMostUnpopularFollower() {
		// TODO Auto-generated method stub
		return maxBlockedUser;
	}

	@Override
	public UUID getLongestMessageThread() {
		// TODO Auto-generated method stub
		return longestThreadUUID;
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


		//Most blocked
		HashSet<String> blockedBy = blockedByMap.get(follower);
		if (blockedBy == null) {
			blockedBy = new HashSet<String>();
		}
		blockedBy.add(user);
		blockedByMap.put(follower,blockedBy);

		if (followersMap.get(user) != null && followersMap.get(user).contains(follower)){
			followersMap.get(user).remove(follower);
		}
		//Update follower Count
		Integer fCount = followerCount.get(user);
		followerCount.put(user, new Integer(fCount.intValue() - 1));
		if (blockedBy.size() > maxBlockedCount){
			maxBlockedCount = blockedBy.size();
			maxBlockedUser = follower;
		} else if(blockedBy.size() > maxBlockedCount) {
			int compared = follower.compareTo(maxBlockedUser);
			if (compared < 0){
				maxBlockedUser = follower;

			}
		}

	}



	public void like(String user, UUID messageId){
		//Check if writer of UUID is not user
		List<String> listMapped = UUIDMap.get(messageId);
		Integer lCount = likeCount.get(messageId);

		if (!listMapped.get(1).equals(user)){
			//Check if already liked
			List<UUID> likedTweets = likedMap.get(user);
			if (likedTweets == null) {
				likedTweets = new ArrayList<>();
			}
			if (lCount == null) {
				lCount = new Integer(0);
			}
				if (!likedTweets.contains(messageId)) {
					likedTweets.add(messageId);
					likedMap.put(user, likedTweets);
					likeCount.put(messageId, new Integer(lCount.intValue() + 1));
				}
			if (maxLikedTweetID == null) {
				maxLikedTweetID = messageId;
			}
			if (lCount > maxLikedTweet){
				maxLikedTweet = lCount;
				maxLikedTweetID = messageId;
			} else if (lCount.intValue() == maxLikedTweet && maxLikedTweetID != null){
				maxLikedTweetID = messageId.compareTo(maxLikedTweetID) < 0 ? messageId : maxLikedTweetID;
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

		//Add thread Position
		messageThreadPosition.put(uuid, 0);

	}


	public void reply(String user, UUID originalMessage, String message, UUID newMessage){
		int messageLength = message.length();
		if(messageLength > longestTweetLength){
			longestTweetLength = messageLength;
		}
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
		List<String> sharedTo =  followersMap.get(user);
		if (sharedTo == null){
			sharedTo = new ArrayList<>();
		}
		List<String>originalMessageList = UUIDMap.get(originalMessage);
		String originalUser = originalMessageList.get(0);
		if (!sharedTo.contains(originalUser)){
			sharedTo.add(originalUser);
		}

		messageSharedToMap.put(newMessage, sharedTo);

		Integer parentThreadPosition = messageThreadPosition.get(originalMessage);
		messageThreadPosition.put(newMessage, 1 + parentThreadPosition);
		//is this the longest thread?

		longestThreadLength = Math.max(longestThreadLength, 1 + parentThreadPosition);
		if (longestThreadUUID == null){
			longestThreadUUID = newMessage;
		}
		if (longestThreadLength == 1 + parentThreadPosition && longestThreadUUID !=null ){
			longestThreadUUID = longestThreadUUID.compareTo(newMessage) < 0 ? newMessage : longestThreadUUID;
		}




		Integer userReplyCount = productivityCount.get(user);
		if (userReplyCount == null){
			userReplyCount = message.length();
		} else {
			userReplyCount = userReplyCount + messageLength;
		}
		productivityCount.put(user, userReplyCount);
		if (longestReplierName == null){
			longestReplierName = user;
		}
		if (longestReplier == userReplyCount.intValue() && longestReplierName != null){
			longestReplierName = user.compareTo(longestReplierName) < 0 ? user : longestReplierName;
		}
		longestReplier = Math.max(longestReplier, userReplyCount);





	}

	public UUID mostSharedMessage(){
		UUID maxKey = null;
		int maxCount = 0;
		for (Map.Entry<UUID, List<String>> entry : messageSharedToMap.entrySet()) {
			if (entry.getValue().size() > maxCount) {
				maxKey = entry.getKey();
				maxCount = entry.getValue().size();
			}
			else if(entry.getValue().size() == maxCount && maxKey != null){
				maxKey = maxKey.compareTo(entry.getKey()) < 0 ? maxKey : entry.getKey();
				maxCount = entry.getValue().size();
 			}
		}
		return maxKey;
	}


	public boolean validateReply(String user, UUID originalMessage, String message){
		List<String> sharedTo = messageSharedToMap.get(originalMessage);
		if (!sharedTo.contains(user)){
			return false;
		}
		HashSet<String> blockedBy = blockedByMap.get(user);
		List<String> originalMessageDetails = UUIDMap.get(originalMessage);
		String originalMessageSender = originalMessageDetails.get(0);
		if (blockedBy != null) {
			if (blockedBy.contains(originalMessageSender)) {
				return false;
			}
		}
		return true;
	}

	public boolean validateLike(String user, UUID messageId){
		List<String> originalMessageDetails = UUIDMap.get(messageId);
		if (originalMessageDetails == null){
			return false;
		}
		List<String> sharedTo = messageSharedToMap.get(messageId);
		if (!sharedTo.contains(user)){
			return false;
		}
		if(originalMessageDetails.get(0).equals(user)){
			return false;
		}
		List<UUID> likedTweets = likedMap.get(user);
		if (likedTweets != null) {
			if (likedTweets.contains(messageId)) {
				return false;
			}
		}
		return true;
	}
}



