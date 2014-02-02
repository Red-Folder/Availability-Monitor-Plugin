package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.Date;

public class Expression {
	
	private String mField;
	private ExpressionType mType;
	private Object mValue;
	
	public Expression(String field, ExpressionType type, Object value) {
		this.mField = field;
		this.mType = type;
		this.mValue = value;
	}
	
	public String toString() {
		String result = this.mField;
		
		if (this.mType == ExpressionType.Equals) result += " = ";
		if (this.mType == ExpressionType.NotEquals) result += " != ";
		if (this.mType == ExpressionType.LessThan) result += " < ";
		if (this.mType == ExpressionType.LessThanOrEquals) result += " <= ";
		if (this.mType == ExpressionType.GreaterThan) result += " > ";
		if (this.mType == ExpressionType.GreaterThanOrEquals) result += " >= ";
		
		if (this.mValue instanceof String) result += "'" + this.mValue + "'";
		if (this.mValue instanceof Date) result += ((Date)this.mValue).getTime();
		if (this.mValue instanceof Integer) result += this.mValue;
		if (this.mValue instanceof Long) result += this.mValue;
		
		return result;
	}

	public enum ExpressionType {
		Equals,
		NotEquals,
		LessThan,
		LessThanOrEquals,
		GreaterThan,
		GreaterThanOrEquals
	}
}
