import React, { useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import type { AuthRequest } from '../types/api';

const validationSchema = yup.object({
  username: yup.string().email('Enter a valid email').required('Email is required'),
  password: yup.string().required('Password is required'),
});

const LoginForm: React.FC = () => {
  const { login, loading, error } = useAuth();
  const navigate = useNavigate();
  const [showRegister, setShowRegister] = useState(false);

  const formik = useFormik<AuthRequest>({
    initialValues: {
      username: '',
      password: '',
    },
    validationSchema,
    onSubmit: async (values) => {
      try {
        await login(values);
        navigate('/');
      } catch (err) {
        // Error is handled by the context
      }
    },
  });

  if (showRegister) {
    return <RegisterForm onBackToLogin={() => setShowRegister(false)} />;
  }

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card mt-5">
            <div className="card-header">
              <h4>Login to Eva Social</h4>
            </div>
            <div className="card-body">
              {error && (
                <div className="alert alert-danger" role="alert">
                  {error}
                </div>
              )}
              <form onSubmit={formik.handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">
                    Email
                  </label>
                  <input
                    type="email"
                    className={`form-control ${
                      formik.touched.username && formik.errors.username
                        ? 'is-invalid'
                        : ''
                    }`}
                    id="username"
                    name="username"
                    value={formik.values.username}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                  {formik.touched.username && formik.errors.username && (
                    <div className="invalid-feedback">{formik.errors.username}</div>
                  )}
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">
                    Password
                  </label>
                  <input
                    type="password"
                    className={`form-control ${
                      formik.touched.password && formik.errors.password
                        ? 'is-invalid'
                        : ''
                    }`}
                    id="password"
                    name="password"
                    value={formik.values.password}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                  {formik.touched.password && formik.errors.password && (
                    <div className="invalid-feedback">{formik.errors.password}</div>
                  )}
                </div>
                <div className="d-grid">
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={loading || !formik.isValid}
                  >
                    {loading ? 'Logging in...' : 'Login'}
                  </button>
                </div>
              </form>
              <div className="text-center mt-3">
                <p>
                  Don't have an account?{' '}
                  <button
                    type="button"
                    className="btn btn-link p-0"
                    onClick={() => setShowRegister(true)}
                  >
                    Sign up here
                  </button>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const RegisterForm: React.FC<{ onBackToLogin: () => void }> = ({ onBackToLogin }) => {
  const { register, loading, error } = useAuth();
  const [success, setSuccess] = useState(false);

  const validationSchema = yup.object({
    email: yup.string().email('Enter a valid email').required('Email is required'),
    password: yup
      .string()
      .min(6, 'Password must be at least 6 characters')
      .required('Password is required'),
    firstName: yup.string().required('First name is required'),
    lastName: yup.string().required('Last name is required'),
    bio: yup.string(),
    profilePictureUrl: yup.string().url('Enter a valid URL'),
  });

  const formik = useFormik({
    initialValues: {
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      bio: '',
      profilePictureUrl: '',
    },
    validationSchema,
    onSubmit: async (values) => {
      try {
        await register(values);
        setSuccess(true);
        setTimeout(() => {
          onBackToLogin();
        }, 2000);
      } catch (err) {
        // Error is handled by the context
      }
    },
  });

  if (success) {
    return (
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-6">
            <div className="alert alert-success mt-5" role="alert">
              <h4>Registration Successful!</h4>
              <p>Your account has been created. Redirecting to login...</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card mt-5">
            <div className="card-header">
              <h4>Register for Eva Social</h4>
            </div>
            <div className="card-body">
              {error && (
                <div className="alert alert-danger" role="alert">
                  {error}
                </div>
              )}
              <form onSubmit={formik.handleSubmit}>
                <div className="row">
                  <div className="col-md-6">
                    <div className="mb-3">
                      <label htmlFor="firstName" className="form-label">
                        First Name
                      </label>
                      <input
                        type="text"
                        className={`form-control ${
                          formik.touched.firstName && formik.errors.firstName
                            ? 'is-invalid'
                            : ''
                        }`}
                        id="firstName"
                        name="firstName"
                        value={formik.values.firstName}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                      />
                      {formik.touched.firstName && formik.errors.firstName && (
                        <div className="invalid-feedback">{formik.errors.firstName}</div>
                      )}
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="mb-3">
                      <label htmlFor="lastName" className="form-label">
                        Last Name
                      </label>
                      <input
                        type="text"
                        className={`form-control ${
                          formik.touched.lastName && formik.errors.lastName
                            ? 'is-invalid'
                            : ''
                        }`}
                        id="lastName"
                        name="lastName"
                        value={formik.values.lastName}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                      />
                      {formik.touched.lastName && formik.errors.lastName && (
                        <div className="invalid-feedback">{formik.errors.lastName}</div>
                      )}
                    </div>
                  </div>
                </div>
                <div className="mb-3">
                  <label htmlFor="email" className="form-label">
                    Email
                  </label>
                  <input
                    type="email"
                    className={`form-control ${
                      formik.touched.email && formik.errors.email
                        ? 'is-invalid'
                        : ''
                    }`}
                    id="email"
                    name="email"
                    value={formik.values.email}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                  {formik.touched.email && formik.errors.email && (
                    <div className="invalid-feedback">{formik.errors.email}</div>
                  )}
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">
                    Password
                  </label>
                  <input
                    type="password"
                    className={`form-control ${
                      formik.touched.password && formik.errors.password
                        ? 'is-invalid'
                        : ''
                    }`}
                    id="password"
                    name="password"
                    value={formik.values.password}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                  {formik.touched.password && formik.errors.password && (
                    <div className="invalid-feedback">{formik.errors.password}</div>
                  )}
                </div>
                <div className="mb-3">
                  <label htmlFor="bio" className="form-label">
                    Bio (Optional)
                  </label>
                  <textarea
                    className="form-control"
                    id="bio"
                    name="bio"
                    rows={3}
                    value={formik.values.bio}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="profilePictureUrl" className="form-label">
                    Profile Picture URL (Optional)
                  </label>
                  <input
                    type="url"
                    className={`form-control ${
                      formik.touched.profilePictureUrl && formik.errors.profilePictureUrl
                        ? 'is-invalid'
                        : ''
                    }`}
                    id="profilePictureUrl"
                    name="profilePictureUrl"
                    value={formik.values.profilePictureUrl}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                  {formik.touched.profilePictureUrl && formik.errors.profilePictureUrl && (
                    <div className="invalid-feedback">{formik.errors.profilePictureUrl}</div>
                  )}
                </div>
                <div className="d-grid">
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={loading || !formik.isValid}
                  >
                    {loading ? 'Creating Account...' : 'Register'}
                  </button>
                </div>
              </form>
              <div className="text-center mt-3">
                <p>
                  Already have an account?{' '}
                  <button
                    type="button"
                    className="btn btn-link p-0"
                    onClick={onBackToLogin}
                  >
                    Login here
                  </button>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;