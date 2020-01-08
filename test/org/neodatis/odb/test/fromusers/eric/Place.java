/**
 * 
 */
package org.neodatis.odb.test.fromusers.eric;

/**
 * @author olivier
 *
 */
public class Place {
	Country dislike;
    Country like;
    public Country getDislike() {
            return dislike;
    }
    public void setDislike(Country dislike) {
            this.dislike = dislike;
    }
    public Country getLike() {
            return like;
    }
    public void setLike(Country like) {
            this.like = like;
    }
}
