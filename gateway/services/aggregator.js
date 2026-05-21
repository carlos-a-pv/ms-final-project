const axios = require('axios');

const EMPLOYEE_SERVICE =process.env.EMPLOYEES_TARGET;
const PROFILE_SERVICE =process.env.PROFILE_TARGET;

const getEmployeeComplete = async (id, token) => {

  const headers = {
    Authorization: token
  };

  const [employee, profile] = await Promise.all([

    axios.get(
      `${EMPLOYEE_SERVICE}/employees/${id}`,
      { headers }
    ),

    axios.get(
      `${PROFILE_SERVICE}/profile/${id}`,
      { headers }
    )

  ]);

  return {
    ...employee.data,
    profile: profile.data
  };
};

module.exports = {
  getEmployeeComplete
};