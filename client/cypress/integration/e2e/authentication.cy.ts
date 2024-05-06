describe('Access control', () => {
  const protectedRoutes = [
    '/dashboard',
    '/profile',
    '/studentApplications',
    '/allCourses',
    '/myApplications',
    '/myCourses',
    '/applyCourse',
    '/createCourse',
    '/myModules',
  ]

  protectedRoutes.forEach(route => {
    it(`Redirects to login when accessing ${route} unauthenticated`, () => {
      cy.visit(route)
      cy.url().should('include', '/login')
    })
  })
})