package yjt.model;

import io.jpress.model.core.Table;
import yjt.model.base.BaseMessage;


@Table(tableName = "message", primaryKey = "id")
public class Message extends BaseMessage<Message>{

	private static final long serialVersionUID = 1L;
	
}