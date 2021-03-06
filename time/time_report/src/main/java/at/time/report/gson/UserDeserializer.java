package at.time.report.gson;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import at.time.report.dao.UserDao;
import at.time.report.model.User;

@Named
public class UserDeserializer implements JsonDeserializer<User> {

	@Inject
	private UserDao userDao;

	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return userDao.getByOid(json.getAsLong());
	}

}
