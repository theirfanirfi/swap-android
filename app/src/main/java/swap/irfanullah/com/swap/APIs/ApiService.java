package swap.irfanullah.com.swap.APIs;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.Comment;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.Like;
import swap.irfanullah.com.swap.Models.Messenger;
import swap.irfanullah.com.swap.Models.Notification;
import swap.irfanullah.com.swap.Models.Participants;
import swap.irfanullah.com.swap.Models.ProfileModel;
import swap.irfanullah.com.swap.Models.Share;
import swap.irfanullah.com.swap.Models.SingleStatusModel;
import swap.irfanullah.com.swap.Models.Statistics;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.Models.User;

public interface ApiService {
    String AFTER_BASE_URL = "http://"+ RetroLib.IP +"/swap/public/api/";
    //String AFTER_BASE_URL = "http://irfitech.com/public/api/";
    @POST(AFTER_BASE_URL+"status/compose")
    @FormUrlEncoded
    Call<Status> composeStatus(@Field("token") String token,@Field("status") String status);

    @GET(AFTER_BASE_URL+"status/getStatuses")
    Call<Status> getStatuses(@Query("token") String token);

    @GET(AFTER_BASE_URL+"status/discoverStatuses")
    Call<Status> discoverStatuses(@Query("token") String token);

    @GET(AFTER_BASE_URL+"status/getUserStatuses")
    Call<Status> getUserStatuses(@Query("id") int id);

    @GET(AFTER_BASE_URL+"followers/")
    Call<Followers> getFollowers(@Query("token") String token, @Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"followers/swapStatus")
    Call<Swap> swapStatus(@Query("token") String token, @Query("swaped_with") int swaped_with_user_id,@Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"followers/deSwapStatus")
    Call<Swap> unSwapStatus(@Query("token") String token, @Query("swaped_with") int swaped_with_user_id,@Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"followers/startFollowing")
    Call<User> getUsersForNacentRegisteration(@Query("token") String token);

    @GET(AFTER_BASE_URL+"followers/followed")
    Call<User> usersFollowed(@Query("token") String token, @Query("followed_count") int count);

    @GET(AFTER_BASE_URL+"followers/invites")
    Call<User> usersInvited(@Query("token") String token);

    @GET(AFTER_BASE_URL+"swaps/")
    Call<SwapsTab> getSwaps(@Query("token") String token);

    @GET(AFTER_BASE_URL+"swaps/user")
    Call<SwapsTab> getUserSwaps(@Query("id") int id);

    @GET(AFTER_BASE_URL+"status/rateStatus")
    Call<Status> rateStatus(@Query("token") String token,@Query("status_id") int status_id, @Query("rating") float rating);
    @GET(AFTER_BASE_URL+"swaps/unswap")
    Call<Swap> unswap(@Query("token") String token,@Query("swap_id") int swap_id);
    @GET(AFTER_BASE_URL+"rating/getStatusRatings")
    Call<SingleStatusModel> getRaters(@Query("token") String token,@Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"rating/getStatusRaters")
    Call<SingleStatusModel> getStatusRaters(@Query("token") String token,@Query("status_id") String status_id);

    @GET(AFTER_BASE_URL+"status/deleteStatus")
    Call<Status> deleteStatus(@Query("token") String token,@Query("status_id") int status_id);

    @Multipart
    @POST(AFTER_BASE_URL+"profile/updateImage")
    Call<ProfileModel> updateProfilePicture(@Part("token") RequestBody token, @Part MultipartBody.Part image);

    @GET(AFTER_BASE_URL+"profile/updateDescription")
    Call<User> updateProfileDescription(@Query("token") String token,@Query("description") String profile_description);

    @GET(AFTER_BASE_URL+"profile/getProfileStats")
    Call<Statistics> getStats(@Query("token") String token);

    @GET(AFTER_BASE_URL+"profile/getProfileUserStats")
    Call<Statistics> getUserStats(@Query("id") int id,@Query("token") String token );

    @GET(AFTER_BASE_URL+"profile/updateProfileDetails")
    Call<User> updateProfileDetails(@Query("token") String token,@Query("name") String name,@Query("username") String username,@Query("email") String email);
    @GET(AFTER_BASE_URL+"profile/changePassword")
    Call<User> changePassword(@Query("token") String token,@Query("newpass") String newpass,@Query("confirmpass") String confirmpass,@Query("oldpass") String oldpass);

    @GET(AFTER_BASE_URL+"notifications/getNotificationsCount")
    Call<Notification> getNotificationsCount(@Query("token") String token);

    @GET(AFTER_BASE_URL+"notifications/getSwapNotificationsCount")
    Call<Notification> getSwapNotificationsCount(@Query("token") String token);

    @GET(AFTER_BASE_URL+"notifications/getNotifications")
    Call<Notification> getNotifications(@Query("token") String token);

    @GET(AFTER_BASE_URL+"notifications/getSwapRequestNotifications")
    Call<Notification> getSwapRequestNotifications(@Query("token") String token);

    @GET(AFTER_BASE_URL+"swaps/getSwap")
    Call<Swap> getSwap(@Query("token") String token,@Query("swap_id") int swap_id);

    @GET(AFTER_BASE_URL+"notifications/approveSwap")
    Call<Swap> approveSwap(@Query("token") String token,@Query("notification_id") int notification_id);

    @GET(AFTER_BASE_URL+"notifications/declineSwap")
    Call<Swap> declineSwap(@Query("token") String token,@Query("notification_id") int notification_id);

    @GET(AFTER_BASE_URL+"notifications/clear")
    Call<Notification> clearNotifications(@Query("token") String token,@Query("id") int notification_id);

    @GET(AFTER_BASE_URL+"followers/follow")
    Call<Followers> follow(@Query("token") String token,@Query("id") int user_id);

    @GET(AFTER_BASE_URL+"followers/unfollow")
    Call<Followers> unfollow(@Query("token") String token,@Query("id") int user_id);

    @GET(AFTER_BASE_URL+"participants/")
    Call<Participants> getParticipants(@Query("token") String token);

    @GET(AFTER_BASE_URL+"msg/")
    Call<Messenger> getMessages(@Query("token") String token,@Query("id") int to_chat_with_id);
    @GET(AFTER_BASE_URL+"msg/send")
    Call<Messenger> sendMessage(@Query("token") String token,@Query("id") int to_chat_with_id,@Query("msg") String message);

    @GET(AFTER_BASE_URL+"msg/getUnReadAndLast")
    Call<Messenger> getUnReadAndLast(@Query("token") String token,@Query("chat_id") int chat_id);


    @GET(AFTER_BASE_URL+"followers/users")
    Call<Followers> getUsers(@Query("token") String token);


    //upload status attachments
    @Multipart
    @POST(AFTER_BASE_URL+"attachments/send")
    Call<Attachments> sendAttachments(@Part("token") RequestBody token, @Part MultipartBody.Part attachment, @Part("attachment_type") RequestBody attachment_type, @Part("status_id") RequestBody status_id);

    @GET(AFTER_BASE_URL+"status/attachments")
    Call<Attachments> getStatusAttachments(@Query("token") String token,@Query("status_id") int status_id);
    @GET(AFTER_BASE_URL+"status/get")
    Call<Status> getStatus(@Query("token") String token,@Query("status_id") int status_id);


    /// search services
    @GET(AFTER_BASE_URL+"search/status")
    Call<Status> searchStatuses(@Query("token") String token,@Query("search") String search);

    @GET(AFTER_BASE_URL+"search/users")
    Call<User> searchUsers(@Query("token") String token,@Query("search") String search);

    @GET(AFTER_BASE_URL+"followers/getUserFollowers")
    Call<User> getUserFollowers(@Query("token") String token,@Query("profile_id") int profile_id);

    //social Login
    @FormUrlEncoded
    @POST(AFTER_BASE_URL+"auth/slogin")
    Call<User> socialLogin(@Field("id") String id, @Field("name") String name, @Field("profile_image") String profile_image, @Field("net") String network);

    //like
    @FormUrlEncoded
    @POST(AFTER_BASE_URL+"like")
    Call<Like> likeOrDislikeStatus(@Field("token") String token,@Field("status_id") String status_id);

    @FormUrlEncoded
    @POST(AFTER_BASE_URL+"share")
    Call<Share> shareStatus(@Field("token") String token, @Field("status_id") String status_id);

    @FormUrlEncoded
    @POST(AFTER_BASE_URL+"comment")
    Call<Comment> commentOnStatus(@Field("token") String token, @Field("status_id") String status_id, @Field("comment") String comment);

    @FormUrlEncoded
    @POST(AFTER_BASE_URL+"comments")
    Call<Comment> getStatusComments(@Field("token") String token, @Field("status_id") String status_id);
}
