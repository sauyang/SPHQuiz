package com.sauyang.webservices.webservices;

import java.util.HashMap;

/**
 * A DataTag represents an identifier for a specific data set
 * stored in the datastore. Typically this data-set is fetched from
 * a web-service, and thus the handler classes are associated
 * with a DataTag.
 *
 */
public class DataTag
{
	private int tagId;
	private String contextTag;
    private HashMap<String, Object> userInfo;

	public DataTag(int tagId, String contextTag, HashMap<String, Object> userInfo)
	{
		this.tagId = tagId;
		this.contextTag = contextTag;
        this.userInfo = userInfo;
	}

	public DataTag(int tagId)
	{
		this.tagId = tagId;
	}
	
	
	public DataTag copyInstance()
	{
		return new DataTag(this.tagId, this.contextTag, this.userInfo);
	}
	
	public DataTag createCopyWithContext(String context)
	{
		return new DataTag(this.tagId, context, this.userInfo);
	}


    public HashMap<String, Object> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(HashMap<String, Object> userInfo) {
        this.userInfo = userInfo;
    }

    public int getTagId()
	{
		return tagId;
	}

	public void setTagId(int tagId)
	{
		this.tagId = tagId;
	}

	public String getContextTag()
	{
		return contextTag;
	}

	public void setContextTag(String contextTag)
	{
		this.contextTag = contextTag;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((contextTag == null) ? 0 : contextTag.hashCode());
		result = prime * result + tagId;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataTag other = (DataTag) obj;
		if (contextTag == null)
		{
			if (other.contextTag != null)
				return false;
		}
		else if (!contextTag.equals(other.contextTag))
			return false;
		if (tagId != other.tagId)
			return false;
		return true;
	}

	

}
