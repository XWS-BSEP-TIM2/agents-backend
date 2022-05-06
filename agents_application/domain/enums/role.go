package enums

type Role int

const (
	USER Role = iota
	COMPANY_OWNER
	ADMIN
)

func (e Role) ToString() string {
	return [...]string{"User", "Company Owner", "Admin"}[e]
}

func ToRoleEnum(role string) Role {
	if role == "User" {
		return USER
	} else if role == "Company Owner" {
		return COMPANY_OWNER
	}
	return ADMIN
}
