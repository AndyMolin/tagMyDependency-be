db.auth('root', 'root')

db = db.getSiblingDB('tag-my-dependency')

db.createUser(
    {
        user: "tagger",
        pwd: "the-tagger-password",
        roles: [
            {
                role: "readWrite",
                db: "tag-my-dependency"
            }
        ]
    }
);
