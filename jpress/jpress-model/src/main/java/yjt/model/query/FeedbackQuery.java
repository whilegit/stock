package yjt.model.query;

import java.math.BigInteger;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Feedback;

public class FeedbackQuery extends JBaseQuery{
	protected static final Feedback DAO = new Feedback();
	private static final FeedbackQuery QUERY = new FeedbackQuery();
	
	public static FeedbackQuery me(){
		return QUERY;
	}
	
	public Feedback findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


