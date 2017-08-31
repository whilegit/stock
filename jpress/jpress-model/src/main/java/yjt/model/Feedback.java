package yjt.model;

import io.jpress.model.core.Table;
import yjt.model.base.BaseFeedback;

@Table(tableName = "feedback", primaryKey = "id")
public class Feedback extends BaseFeedback<Feedback>{

	private static final long serialVersionUID = 1L;
}
